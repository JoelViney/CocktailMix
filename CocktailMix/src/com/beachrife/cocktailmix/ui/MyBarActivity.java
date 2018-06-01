package com.beachrife.cocktailmix.ui;

import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.beachrife.cocktailmix.ApplicationEx;
import com.beachrife.cocktailmix.R;
import com.beachrife.cocktailmix.helpers.ConsoleHelper;

public class MyBarActivity extends FragmentActivity implements ActionBar.TabListener 
{
	private static int TOTAL_PAGES = 4;
	
	// The PagerAdapter that will provide fragments for each of the sections. 
	// We use a FragmentPagerAdapter derivative, which will keep every loaded fragment in memory.
	// If this becomes too memory intensive, it may be best to switch to a FragmentStatePagerAdapter.
	private SectionsPagerAdapter sectionsPagerAdapter;

	// The ViewPager that will host the section contents.
	private ViewPager viewPager;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		ConsoleHelper.writeLine("MyBarActivity.onCreate()");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_bar_activity);
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar(); 
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true); // Show the Up button in the action bar.

		// Create the adapter that will return a fragment for each of the three primary sections of the app.
		ApplicationEx context = (ApplicationEx)this.getApplicationContext();
		sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), context);

		// Set up the ViewPager with the sections adapter.
		this.viewPager = (ViewPager) findViewById(R.id.pager);
		this.viewPager.setAdapter(sectionsPagerAdapter);

		// When swiping between different sections, select the corresponding tab. 
		// We can also use ActionBar.Tab#select() to do this if we have a reference to the Tab.
		this.viewPager.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener() 
				{
					@Override
					public void onPageSelected(int position) 
					{
						ConsoleHelper.writeLine("MyBarActivity.viewPager.onPageSelected()");
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < sectionsPagerAdapter.getCount(); i++) 
		{
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			Tab tab = actionBar.newTab();
			tab.setText(sectionsPagerAdapter.getPageTitle(i));
			tab.setTabListener(this);
			actionBar.addTab(tab);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.default_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case android.R.id.home: // This ID represents the Home or Up button. In the case of this activity, the Up button is shown. Use NavUtils to allow users to navigate up one level in the application structure. 
			NavUtils.navigateUpTo(this, new Intent(this, DrinkListActivity.class));
			return true;

			case R.id.action_settings:
	    	Intent intent = new Intent(this, SettingsActivity.class);
	    	startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
	{
		ConsoleHelper.writeLine("MyBarActivity.onTabSelected()");
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
	{
		
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
	{
		
	}
	
	
	
	// A FragmentPagerAdapter that returns a fragment corresponding to one of the sections/tabs/pages.
	public class SectionsPagerAdapter extends FragmentPagerAdapter 
	{
		private ApplicationEx context;
		
		public SectionsPagerAdapter(FragmentManager fm, ApplicationEx context) 
		{
			super(fm);
			this.context = context;
		}

		@Override
		public Fragment getItem(int position) 
		{
			ConsoleHelper.writeLine("MyBarActivity.SectionsPagerAdapter.getItem(%d)", position);
			// At this point we could pass in the bar items if we wanted to.
			// But currently I am happy that the Fragment builds it's own list.
			Fragment fragment = new MyBarTabPanelFragment(this.context);
			Bundle arguments = new Bundle();

			arguments.putInt(MyBarTabPanelFragment.INGREDIENT_FILTER_ARGUMENT, position + 1);
			fragment.setArguments(arguments);
			return fragment;
		}

		@Override
		public int getCount() 
		{
			return TOTAL_PAGES;
		}

		@Override
		public CharSequence getPageTitle(int position) 
		{
			Locale l = Locale.getDefault();
			switch (position) 
			{
			case 0: return getString(R.string.my_bar_tab_title_tab_section1).toUpperCase(l);
			case 1: return getString(R.string.my_bar_tab_title_tab_section2).toUpperCase(l);
			case 2: return getString(R.string.my_bar_tab_title_tab_section3).toUpperCase(l);
			case 3: return getString(R.string.my_bar_tab_title_tab_section4).toUpperCase(l);
			}
			return null;
		}
	}
	
	

    @Override
    public void onResume()
    {
    	super.onResume();

    	// Reset the selected item from the list view. 
        ApplicationEx context = (ApplicationEx)getApplicationContext();
        context.setSelectedDrinkName(null);
    }
}
