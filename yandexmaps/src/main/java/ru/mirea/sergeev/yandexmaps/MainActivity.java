package ru.mirea.sergeev.yandexmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends AppCompatActivity implements UserLocationObjectListener {
    private MapView mapView;
    private final String MAPKIT_API_KEY = "b0532fdf-b09b-45af-8b4d-044087102c19";
    private UserLocationLayer userLocationLayer;
    private static final String TAG = "MapActivity";
    private static final int REQUEST_CODE_PERMISSION = 100;
    private String PERMISSION[] = {android.Manifest.permission.ACCESS_FINE_LOCATION};
    private boolean isWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/**
 * Задайте API-ключ перед инициализацией MapKitFactory.
 * Рекомендуется устанавливать ключ в методе Application.onCreate,
 * но в данном примере он устанавливается в activity.
 */
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
/**
 * Инициализация библиотеки для загрузки необходимых нативных библиотек.
 * Рекомендуется инициализировать библиотеку MapKit в методе Activity.onCreate
 * Инициализация в методе Application.onCreate может привести к лишним вызовам и
 увеличенному использованию батареи.
 */
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
// Укажите имя activity вместо map.
        mapView = findViewById(R.id.mapview);
        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        isWork = hasPermissions(this, PERMISSION);
        if (!isWork) {
            ActivityCompat.requestPermissions(this, PERMISSION,
                    REQUEST_CODE_PERMISSION);
        }
    }

    private void loadUserLocationLayer(){
        MapKit mapKit = MapKitFactory.getInstance();
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true) ;
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
            int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
// permission granted
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
// Вызов onStop нужно передавать инстансам MapView и MapKit.
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }
    @Override
    protected void onStart() {
// Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {

        userLocationLayer.setAnchor(
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83)));
// При определении направления движения устанавливается следующая иконка
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                this,android.R.drawable.star_big_on ));
// При получении координат местоположения устанавливается следующая иконка
        userLocationView.getPin().setIcon(ImageProvider.fromResource(
                this, android.R.drawable.ic_menu_mylocation));
        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE);
        loadUserLocationLayer();
    }
    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {
    }
    @Override

    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }


}