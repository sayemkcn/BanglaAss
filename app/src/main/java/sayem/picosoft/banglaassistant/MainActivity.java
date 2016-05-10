package sayem.picosoft.banglaassistant;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sayem.picosoft.banglaassistant.adapter.ProcessAdapter;
import sayem.picosoft.banglaassistant.helper.PageMainHelper;
import sayem.picosoft.banglaassistant.helper.PageOperationHelper;
import sayem.picosoft.banglaassistant.helper.PageProcessHelper;
import sayem.picosoft.banglaassistant.model.SingleProcessItem;
import sayem.picosoft.banglaassistant.service.UsageService;

public class MainActivity extends AppCompatActivity {

    private static Intent serviceIntent;
    private static List<SingleProcessItem> processList;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private PageProcessHelper mPageProcessHelper;

    int mKilledAppCount = 0;

    @Override
    protected void onPause() {
        super.onPause();
        stopService(serviceIntent);
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(PageMainHelper.BROADCASTRECEIVER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (serviceIntent == null) {
            serviceIntent = new Intent(getApplicationContext(), UsageService.class);
        }
        startService(serviceIntent);
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//                PageMainHelper.BROADCASTRECEIVER, new IntentFilter("UsageUpdate"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (serviceIntent == null) {
            serviceIntent = new Intent(getApplicationContext(), UsageService.class);
        }
        startService(serviceIntent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mPageProcessHelper = new PageProcessHelper(this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("PAGE","position: "+position+"\nPosition Offset: "+positionOffset+"\nPosition Offset Pixels: "+positionOffsetPixels);
//                if (position==1){
//                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
//                    rootView = inflater.inflate(R.layout.fragment_process, null, false);
//                    RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.processRecyclerView);
//                    recyclerView.setAdapter(new ProcessAdapter(getApplicationContext(), new PageProcessHelper(MainActivity.this).getProcessList()));
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext().getApplicationContext()));
//                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Please wait..");
                progressDialog.setMessage("Killing background processes and Boosting your phone.");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mKilledAppCount = mPageProcessHelper.killBackgroundProcesses();
                        if (progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }


                        Snackbar snackbar = Snackbar.make(view, "Your phone has been boosted! \nCleaned " + mKilledAppCount + " apps", Snackbar.LENGTH_LONG)
                                .setAction("Action", null);
                        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
                        layout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        snackbar.show();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int currentItem = mViewPager.getCurrentItem();
                                mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
                                mViewPager.setCurrentItem(currentItem);
                            }
                        });

                    }
                }).start();


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            TextView textView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    PageMainHelper pageMainHelper = new PageMainHelper(getActivity(), rootView);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_process, container, false);
                    final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.processRecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            processList = new PageProcessHelper(getActivity()).getProcessList();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setAdapter(new ProcessAdapter(getActivity(),processList));
                                }
                            });
                        }
                    }).start();
//                    recyclerView.setAdapter(new ProcessAdapter(getActivity(), new PageProcessHelper(getActivity()).getProcessList()));

                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_tools, container, false);
                    PageOperationHelper helper = PageOperationHelper.newInstance(getActivity(), rootView);
                    helper.initAndSetOperationToolsPage();
                    break;
                default:
                    break;
            }

            return rootView;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }


    }

}
