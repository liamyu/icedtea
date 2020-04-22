package com.cang;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Api annotation processor.
 * <p>
 * debug cmd: gradle --no-daemon -Dorg.gradle.debug=true :app:clean :app:compileStagingDebugJavaWithJavac
 * <p>
 * ref: https://github.com/square/javapoet
 * <p>
 * Created by Liam on 2017/3/29.
 */
//@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"Api", "Host", "ParamField"})
public class ApiAnnotationProcessor extends AbstractProcessor {

  private static final String PACKAGE_NAME = "com.cang";
  private static final String FIELD_NAME = "service";
  private Filer filer;

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    final Set<String> supportedAnnotationTypes = new LinkedHashSet<>();
    supportedAnnotationTypes.add(Api.class.getName());
    supportedAnnotationTypes.add(Host.class.getName());
    supportedAnnotationTypes.add(ParamField.class.getName());
    return supportedAnnotationTypes;
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnvironment) {
    super.init(processingEnvironment);
    Messager messager = processingEnvironment.getMessager();
    this.filer = processingEnvironment.getFiler();
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    generateInterface(roundEnvironment.getElementsAnnotatedWith(Api.class));
    generateApi(roundEnvironment.getElementsAnnotatedWith(Api.class));
    return true;
  }

  /**
   * Generate retrofit service interface.
   */
  private void generateInterface(Set<? extends Element> elements) {
    for (Element element : elements) {
      String clzName = element.getSimpleName().toString();
      try {
        Builder interfaceBuilder = TypeSpec.interfaceBuilder(clzName)
            .addModifiers(Modifier.PUBLIC);
        generateInterfaceMethods(interfaceBuilder, element);
        TypeSpec type = interfaceBuilder.build();
        JavaFile file = JavaFile.builder(PACKAGE_NAME, type).build();
        file.writeTo(filer);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * process java methods.
   *
   * @param interfaceBuilder print stream
   * @param element element
   */
  private void generateInterfaceMethods(Builder interfaceBuilder, Element element) {
    List<? extends Element> enclosedElements = element.getEnclosedElements();
    enclosedElements.stream()
        .filter(subElement -> subElement.getKind() == ElementKind.METHOD)
        .forEach(subElement -> {
          Cmd cmd = subElement.getAnnotation(Cmd.class);
          if (cmd == null) {
            return;
          }
          Host host = subElement.getAnnotation(Host.class);
          String service = Host.com;
          if (host != null) {
            service = host.value();
          }

          long partCount = ((ExecutableElement) subElement).getParameters()
              .stream()
              .filter(var -> var.getAnnotation(Part.class) != null)
              .count();

          String methodName = subElement.getSimpleName().toString();
          TypeName returnType = TypeName.get(((ExecutableElement) subElement).getReturnType());
          AnnotationSpec postAnnotation = AnnotationSpec.builder(POST.class)
              .addMember("value", "$S",
                  String.format("%s?c=1&cmd=%s&f=json&t=[t]&v=%d&sign=[s]", service, cmd.value(),
                      cmd.version()))
              .build();
          MethodSpec.Builder methodBuilder = MethodSpec
              .methodBuilder(methodName)
              .returns(returnType)
              .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

          // don't know why, but this statement must be placed here to make Multipart Annotation present.
          if (partCount > 0) {
            // add Multipart annotation when Part annotation present
            methodBuilder.addAnnotation(Multipart.class);
          } else {
            // only add FormUrlEncoded annotation when ParamField exists
            if (((ExecutableElement) subElement).getParameters()
                .stream().anyMatch(var -> var.getAnnotation(ParamField.class) != null)) {
              methodBuilder.addAnnotation(FormUrlEncoded.class);
            }
          }

          methodBuilder.addAnnotation(postAnnotation);

          // iterate parameters, and add to method.
          // process parameters
          Object[] array = ((ExecutableElement) subElement).getParameters()
              .stream()
              .filter(var -> var.getAnnotation(ParamField.class) != null)
              .toArray();
          if (array.length > 0) {
            AnnotationSpec.Builder annotationBuilder;
            if (partCount > 0) {
              annotationBuilder = AnnotationSpec.builder(Part.class);
            } else {
              annotationBuilder = AnnotationSpec.builder(Field.class);
            }
            annotationBuilder.addMember("value", "$S", "p");
            ParameterSpec.Builder businessParameterBuilder;
            if (partCount > 0) {
              businessParameterBuilder = ParameterSpec.builder(RequestBody.class, "p")
                  .addAnnotation(annotationBuilder.build());
            } else {
              businessParameterBuilder = ParameterSpec.builder(String.class, "p")
                  .addAnnotation(annotationBuilder.build());
            }
            methodBuilder.addParameter(businessParameterBuilder.build());
          }

          // copy part parameters
          ((ExecutableElement) subElement).getParameters()
              .stream()
              .filter(var -> var.getAnnotation(Part.class) != null)
              .forEach(var -> {
                AnnotationSpec partAnnotationSpec = AnnotationSpec.builder(Part.class)
                    //                    .addMember("value", "$S", var.getAnnotation(Part.class).value())
                    .build();
                ParameterSpec partParameter = ParameterSpec.builder(TypeName.get(var.asType()),
                    var.getSimpleName().toString())
                    .addAnnotation(partAnnotationSpec)
                    .build();

                methodBuilder.addParameter(partParameter);
              });

          MethodSpec methodSpec = methodBuilder.build();
          interfaceBuilder.addMethod(methodSpec);
        });
  }

  /**
   * Generate api service implementation.
   */
  private void generateApi(Set<? extends Element> elements) {
    for (Element element : elements) {
      String serviceName = element.getSimpleName().toString();
      String clzName = String.format("%sImpl", serviceName);
      try {
        Builder classBuilder = TypeSpec.classBuilder(clzName)
            .addModifiers(Modifier.PUBLIC);
        generateServiceField(serviceName, classBuilder);
        generateApiMethods(classBuilder, element);
        TypeSpec type = classBuilder.build();
        JavaFile file = JavaFile.builder(PACKAGE_NAME, type).build();
        file.writeTo(filer);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * add static service instance field.
   *
   * @param serviceName service name.
   * @param classBuilder class builder.
   */
  private void generateServiceField(String serviceName, Builder classBuilder) {
    ClassName retrofitProvider = ClassName
        .get("com.cang.collector.common.utils.network.retrofit", "RetrofitProvider");
    ClassName serviceInterface = ClassName.get(PACKAGE_NAME, serviceName);
    FieldSpec.Builder fieldBuilder = FieldSpec
        .builder(serviceInterface, FIELD_NAME, Modifier.PRIVATE, Modifier.STATIC)
        .initializer("$T.get().create($T.class)", retrofitProvider, serviceInterface);
    classBuilder.addField(fieldBuilder.build());
  }

  /**
   * process java methods.
   *
   * @param classBuilder print stream
   * @param element element
   */
  private void generateApiMethods(Builder classBuilder, Element element) {
    List<? extends Element> enclosedElements = element.getEnclosedElements();
    enclosedElements.stream().filter(subElement ->
        subElement.getKind() == ElementKind.METHOD
            && subElement.getAnnotation(Cmd.class) != null)
        .forEach(subElement -> {
          String methodName = subElement.getSimpleName().toString();
          TypeName returnType = TypeName.get(((ExecutableElement) subElement).getReturnType());
          MethodSpec.Builder methodBuilder = MethodSpec
              .methodBuilder(methodName)
              .returns(returnType)
              .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

          // iterate business parameters, and add to method.
          ((ExecutableElement) subElement)
              .getParameters()
              .stream()
              .filter(var -> var.getAnnotation(ParamField.class) != null)
              .forEach(var -> {
                ParameterSpec businessParameter = ParameterSpec
                    .builder(TypeName.get(var.asType()), var.getSimpleName().toString())
                    .build();
                methodBuilder.addParameter(businessParameter);
              });

          // copy part parameters
          ((ExecutableElement) subElement).getParameters()
              .stream()
              .filter(var -> var.getAnnotation(Part.class) != null)
              .forEach(var -> {
                ParameterSpec partParameter;
                if (var.asType().getKind() == TypeKind.ARRAY) {
                  partParameter = ParameterSpec
                      .builder(File[].class, var.getSimpleName().toString())
                      .build();
                } else {
                  partParameter = ParameterSpec
                      .builder(File.class, var.getSimpleName().toString())
                      .build();
                }
                methodBuilder.addParameter(partParameter);
              });

          generateStatements(methodBuilder, (ExecutableElement) subElement);

          MethodSpec methodSpec = methodBuilder.build();
          classBuilder.addMethod(methodSpec);
        });
  }

  /**
   * Generate method statements.
   */
  private void generateStatements(MethodSpec.Builder methodBuilder,
      ExecutableElement methodElement) {
    ClassName jsonBuilder = ClassName.get("com.liam.iris.utils", "JsonBuilder");
    ClassName scheduler = ClassName.get("io.reactivex.schedulers", "Schedulers");
    ClassName androidScheduler = ClassName
        .get("io.reactivex.android.schedulers", "AndroidSchedulers");

    // use JsonBuilder to build business params.
    long businessParamCount = methodElement.getParameters()
        .stream()
        .filter(var -> var.getAnnotation(ParamField.class) != null).count();
    if (businessParamCount > 0) { // with business parameters.
      CodeBlock.Builder codeBlockBuilder = CodeBlock.builder()
          .add("$T s = new $T()\n", String.class, jsonBuilder)
          .indent();
      methodElement.getParameters()
          .stream()
          .filter(var -> var.getAnnotation(ParamField.class) != null)
          .forEach(var -> {
            String key = var.getAnnotation(ParamField.class).value();
            String paramName = var.getSimpleName().toString();
            if (key.length() < 1) { // use parameter name if ParamField specifies no value.
              key = paramName;
            }
            codeBlockBuilder.add(".append($S, $L)\n", key, paramName);
          });
      codeBlockBuilder.add(".toString();\n")
          .unindent();
      CodeBlock codeBlock = codeBlockBuilder.build();
      methodBuilder.addCode(codeBlock);

      long partCount = methodElement.getParameters()
          .stream()
          .filter(var -> var.getAnnotation(Part.class) != null).count();
      if (partCount > 0) {  // with multipart.
        // instantiate "p" param RequestBody
        methodBuilder.addStatement("$T param = $T.create($T.FORM, s)",
            RequestBody.class, RequestBody.class, MultipartBody.class);

        List<String> filePartList = new ArrayList<>();
        methodBuilder.addStatement("$T fileName", String.class);
        methodBuilder.addStatement("$T mediaType", MediaType.class);
        methodBuilder.addStatement("$T requestFile", RequestBody.class);
        methodElement.getParameters()
            .stream()
            .filter(var -> var.getAnnotation(Part.class) != null)
            .forEach(var -> {
              String filePartName = String.format("%sPart", var.getSimpleName());
              if (var.asType().getKind() == TypeKind.ARRAY) {
                // array of parts
                methodBuilder.addCode(CodeBlock.builder()
                    .addStatement("$T[] $L = new $T[$L.length]", MultipartBody.Part.class,
                        filePartName,
                        MultipartBody.Part.class, var.getSimpleName())
                    .beginControlFlow("for (int i = 0; i < $L.length; i++)",
                        var.getSimpleName())
                    .addStatement("fileName = $L[i].getName()", var.getSimpleName())
                    .addStatement("mediaType = $T.parse($T.guessContentTypeFromName(fileName))",
                        MediaType.class, URLConnection.class)
                    .addStatement("requestFile = $T.create(mediaType, $L[i])", RequestBody.class,
                        var.getSimpleName())
                    .addStatement("$L[i] = $T.createFormData($S + i, fileName, requestFile)",
                        filePartName, MultipartBody.Part.class,
                        var.getAnnotation(Part.class).value())
                    .endControlFlow()
                    .build());
              } else {
                addPart(methodBuilder, var, filePartName);
              }
              filePartList.add(filePartName);
            });
        methodBuilder.addCode("return service.$L(param", methodElement.getSimpleName());
        Iterator<String> iterator = filePartList.iterator();
        do {
          methodBuilder.addCode(" ,$L", iterator.next());
        } while (iterator.hasNext());
        methodBuilder.addCode(")\n");
      } else {  // without multipart.
        methodBuilder.addCode("return service.$L(s)\n", methodElement.getSimpleName());
      }
    } else {  // without business parameters.
      long partCount = methodElement.getParameters()
          .stream()
          .filter(var -> var.getAnnotation(Part.class) != null).count();
      if (partCount > 0) {  // with multipart.
        List<String> filePartList = new ArrayList<>();
        methodBuilder.addStatement("$T fileName", String.class)
            .addStatement("$T mediaType", MediaType.class)
            .addStatement("$T requestFile", RequestBody.class);
        methodElement.getParameters()
            .stream()
            .filter(var -> var.getAnnotation(Part.class) != null)
            .forEach(var -> {
              String filePartName = String.format("%sPart", var.getSimpleName());
              addPart(methodBuilder, var, filePartName);
              filePartList.add(filePartName);
            });
        methodBuilder.addCode("return service.$L(", methodElement.getSimpleName());
        Iterator<String> iterator = filePartList.iterator();
        do {
          methodBuilder.addCode("$L", iterator.next());
          if (iterator.hasNext()) {
            methodBuilder.addCode(", ");
          }
        } while (iterator.hasNext());
        methodBuilder.addCode(")\n");
      } else {  // without multipart.
        methodBuilder.addCode("return service.$L()\n", methodElement.getSimpleName());
      }
    }
    CodeBlock.Builder codeBlockBuilder = CodeBlock.builder().indent();
    codeBlockBuilder.add(".subscribeOn($T.io())\n", scheduler);
    codeBlockBuilder.addStatement(".observeOn($T.mainThread())", androidScheduler);
    codeBlockBuilder.unindent();
    methodBuilder.addCode(codeBlockBuilder.build());
  }

  /**
   * Add one multipart.
   */
  private void addPart(MethodSpec.Builder methodBuilder,
      VariableElement var, String filePartName) {
    methodBuilder.addStatement("fileName = $L.getName()", var.getSimpleName())
        .addStatement("mediaType = $T.parse($T.guessContentTypeFromName(fileName))",
            MediaType.class, URLConnection.class)
        .addStatement("requestFile = $T.create(mediaType, $L)", RequestBody.class,
            var.getSimpleName())
        .addStatement("$T $L = $T.createFormData($S, fileName, requestFile)",
            MultipartBody.Part.class, filePartName, MultipartBody.Part.class,
            var.getAnnotation(Part.class).value());
  }

  //  /**
  //   * Process business params.
  //   *
  //   * @param ps print stream
  //   * @param element element
  //   */
  //  private void processParamFields(PrintStream ps, Element element) {
  //    List<? extends VariableElement> variableElements = ((ExecutableElement) element)
  //        .getParameters();
  //    for (int i = 0, variableElementsSize = variableElements.size(); i < variableElementsSize; i++) {
  //      VariableElement variableElement = variableElements.get(i);
  //      if (variableElement.getAnnotation(ParamField.class) != null) {
  //        String type = variableElement.asType().toString();
  //        String name = variableElement.getSimpleName().toString();
  //        if (i != variableElementsSize - 1) {
  //          ps.print(String.format("%s %s, ", type, name));
  //        } else {
  //          ps.print(String.format("%s %s", type, name));
  //        }
  //      }
  //    }
  //  }
  //
  //  /**
  //   * Process business params construction.
  //   *
  //   * @param ps print stream
  //   * @param element element
  //   */
  //  private void processJsonBuilder(PrintStream ps, Element element) {
  //    List<? extends VariableElement> variableElements = ((ExecutableElement) element)
  //        .getParameters();
  //    for (int i = 0, variableElementsSize = variableElements.size(); i < variableElementsSize; i++) {
  //      VariableElement variableElement = variableElements.get(i);
  //      if (variableElement.getAnnotation(ParamField.class) != null) {
  //        String type = variableElement.asType().toString();
  //        String name = variableElement.getSimpleName().toString();
  //        ps.println(String.format("                .append(\"%s\", %s)", type, name));
  //      }
  //    }
  //  }

}
