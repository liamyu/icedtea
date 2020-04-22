package com.liam.tablayout

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.NinePatch
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.liam.tablayout.ui.main.SectionsPagerAdapter
import android.widget.TextView
import android.view.ViewGroup
import android.widget.LinearLayout
import android.R.attr.bitmap
import android.graphics.drawable.Drawable





class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        class OnTabSelectedListener : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(selectedTab: TabLayout.Tab) {
                val tabLayout1 = (tabs.getChildAt(0) as ViewGroup).getChildAt(selectedTab.position) as LinearLayout
                val tabTextView = tabLayout1.getChildAt(1) as TextView
                // tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.BOLD);
                tabTextView.textSize = 80f
                tabTextView.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }

            override fun onTabUnselected(unselectedTab: TabLayout.Tab) {
                val tabLayout1 = (tabs.getChildAt(0) as ViewGroup).getChildAt(unselectedTab.position) as LinearLayout
                val tabTextView = tabLayout1.getChildAt(1) as TextView
                //tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.NORMAL);
                tabTextView.textSize = 15f
                tabTextView.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        }
        tabs.addOnTabSelectedListener(OnTabSelectedListener())
        tabs.setupWithViewPager(viewPager)

//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.tab_indicator)
//        NinePatch
//        val np = NinePatchBitmapFactory.createNinePathWithCapInsets(resources, bitmap, 15, 15, 16,
//            16, null)
//
//        tabs.setSelectedTabIndicator()
        tabs.getTabAt(0)?.setIcon(R.drawable.tab_indicator2)
        tabs.getTabAt(1)?.setIcon(R.drawable.tab_indicator2)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    fun pad(Src: Bitmap, padding_x: Float, padding_y: Float): Bitmap {
        val outputimage = Bitmap.createBitmap((Src.width + padding_x).toInt(),
            (Src.height + padding_y).toInt(),
            Bitmap.Config.ARGB_8888)
        val can = Canvas(outputimage)
        can.drawARGB(0, 0, 0, 0) //This represents White color
        can.drawBitmap(Src, padding_x, padding_y, null)
        return outputimage
    }

}