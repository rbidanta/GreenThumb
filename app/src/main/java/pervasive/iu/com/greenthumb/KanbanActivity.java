package pervasive.iu.com.greenthumb;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import pervasive.iu.com.greenthumb.DBHandler.GardenInfo;
import pervasive.iu.com.greenthumb.GardenPartner.TabLayoutFragment.BacklogFragment;
import pervasive.iu.com.greenthumb.GardenPartner.TabLayoutFragment.CompletedFragment;
import pervasive.iu.com.greenthumb.GardenPartner.TabLayoutFragment.GardenOverview;
import pervasive.iu.com.greenthumb.GardenPartner.TabLayoutFragment.ToBeDoneFragment;

public class KanbanActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager tabsviewpager;
    private Bundle bundle = new Bundle();
    private CoordinatorLayout main_content;
    private GardenInfo gInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanban);

        main_content = (CoordinatorLayout) findViewById(R.id.main_content);

        gInfo = (GardenInfo) getIntent().getSerializableExtra("gInfo");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        tabsviewpager = (ViewPager) findViewById(R.id.tabsviewpager);
        setUpViewPager(tabsviewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(tabsviewpager);

        tabLayout.getTabAt(3).setIcon(R.drawable.gamification);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        GardenOverview gardenOverview = new GardenOverview();
        bundle = new Bundle();
        bundle.putSerializable("gInfo",gInfo);
        gardenOverview.setArguments(bundle);

        BacklogFragment backlogFragment = new BacklogFragment();
        bundle = new Bundle();
        bundle.putSerializable("gInfo",gInfo);
        backlogFragment.setArguments(bundle);

        ToBeDoneFragment toBeDoneFragment = new ToBeDoneFragment();
        bundle = new Bundle();
        bundle.putSerializable("gInfo",gInfo);
        toBeDoneFragment.setArguments(bundle);

        CompletedFragment completedFragment = new CompletedFragment();
        bundle = new Bundle();
        bundle.putSerializable("gInfo",gInfo);
        completedFragment.setArguments(bundle);

        adapter.addFragment(gardenOverview, "OverView");
        adapter.addFragment(backlogFragment, "Backlog");
        adapter.addFragment(toBeDoneFragment, "To Be Done");
        adapter.addFragment(completedFragment, "");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kanban, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
