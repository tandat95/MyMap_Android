package com.vbd.mapexam;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.vietbando.vietbandosdk.annotations.Polyline;
import com.vietbando.vietbandosdk.annotations.PolylineOptions;
import com.vietbando.vietbandosdk.camera.CameraUpdateFactory;
import com.vietbando.vietbandosdk.constants.Style;
import com.vietbando.vietbandosdk.geometry.LatLng;
import com.vietbando.vietbandosdk.maps.MapView;
import com.vietbando.vietbandosdk.maps.OnMapReadyCallback;
import com.vietbando.vietbandosdk.maps.VietbandoMap;
import com.vietbando.vietbandosdk.style.layers.Layer;
import com.vietbando.vietbandosdk.style.layers.SymbolLayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import adapter.SearchResultAdapter;
import model.SearchResultObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MapView mapView;
    VietbandoMap map;
    RouteData data;
    Polyline polyline;
    int pos = 0;
    ListView lvSuggessSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == android.R.id.home) {
                    lvSuggessSearch.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int a = item.getItemId();
                Toast.makeText(MainActivity.this, a + "", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(Style.VIETBANDO_VT_DEFAULT);

        data = initData();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(VietbandoMap vietbandoMap) {
                vietbandoMap.easeCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.7789506, 106.655651), 12), 0);
                map = vietbandoMap;


            }
        });
        findStreet();
    }


    private int lastLineLayerIndex() {
        List<Layer> layers = map.getLayers();
        for (int i = 0, n = layers.size(); i < n; i++) {
            Layer l = layers.get(i);
            if (l instanceof SymbolLayer)
                return i;
        }
        return layers.size();
    }

    private void addPolilyne() {
        if (map == null) {
            return;
        }
        if (polyline != null) {
            map.removePolyline(polyline);
            polyline = null;
        }
        PolylineOptions options = new PolylineOptions();
        options.color(0xFF33B5E5);
        if (pos >= data.route.length) {
            pos = 0;
        }
        double[] path = data.route[pos].path;
        for (int i = 0, n = path.length; i < n; i += 2) {
            options.add(new LatLng(path[i + 1], path[i]));
        }
        //prolong path
//        int nextPos = pos + 1;
//        if (nextPos >= data.route.length) {
//            nextPos = 0;
//        }
//        path = data.route[nextPos].path;
//        for (int i = 0, n = path.length; i < n; i+=2) {
//            options.add(new LatLng(path[i + 1], path[i]));
//        }
        polyline = map.addPolyline(options);
        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(path[1], path[0])));
    }

    private RouteData initData() {
        try {
            InputStream in = getAssets().open("test.json");
            String tmp = convertStreamToString(in);
            Gson gson = new Gson();
            RouteData data = gson.fromJson(tmp, RouteData.class);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvSuggessSearch = (ListView) findViewById(R.id.listViewSuggessSearch);
                ArrayList<SearchResultObject> arrayList = new ArrayList<>();
                String name[] ={"cafe 1", "cafe 2", "cafe 3", "cafe4"};
                String adress[] ={"329 Lý Thường Kiệt Tân Bình HCM", "335 Lý Thường Kiệt Tân Bình HCM", "336 Lý Thường Kiệt Tân Bình HCM", "337 Lý Thường Kiệt Tân Bình HCM"};
                for (int i=0; i<name.length; i++){
                    SearchResultObject obj = new SearchResultObject();
                    obj.setName(name[i]);
                    obj.setAdress(adress[i]);
                    arrayList.add(obj);
                }
                SearchResultAdapter searchResultAdapter = new SearchResultAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                lvSuggessSearch.setAdapter(searchResultAdapter);
                lvSuggessSearch.setVisibility(View.VISIBLE);
            }
        });

        return true;
    }

    public class RouteData {
        @SerializedName("Route")
        public Route[] route;
    }

    public class Route {
        @SerializedName("FullPath")
        public double[] path;
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void findStreet (){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Tìm đường");
        dialogBuilder.setView(this.getLayoutInflater().inflate(R.layout.search_street_dialog, null));
        AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.show();
    }
}