package ru.ivasev.aggregator.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import org.michaelevans.colorart.library.ColorArt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.ivasev.aggregator.MainActivity;


public class Card {
    private static SQLiteDatabase db;
    private static File storageDir;
    private static MainActivity mainContext;
    public static final int REQUEST_TAKE_LOGO = 1;
    public static final int REQUEST_TAKE_BARCODE = 2;


    private long id;
    public String name;
    public String description;
    public String code;
    public String color;
    public String logo;
    public String barcode;

    public long getId() {
        return id;
    }

    private static void init(Context context) {
        mainContext = (MainActivity)context;
        storageDir = context.getFilesDir();
        CardDbHelper cardDbHelper = new CardDbHelper(context);
        db = cardDbHelper.getWritableDatabase();
    }

    public Card(Context context, long id, String name, String description, String code, String color, String logo, String barcode) {
        if (db == null) {
            Card.init(context);
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.code = code;
        this.color = color;
        this.logo = logo;
        this.barcode = barcode;
    }


    public Card(Context context, String name, String description, String code, String color, String logo, String barcode) {
        if (db == null) {
            Card.init(context);
        }
        this.id = -1;
        this.name = name;
        this.description = description;
        this.code = code;
        this.color = color;
        this.logo = logo;
        this.barcode = barcode;
    }

    public static List<Card> getList(Context context, String[] projection, String selection, String[] selectionArgs) {
        if (db == null) {
            Card.init(context);
        }
        List<Card> imageList = new ArrayList<>();
        if (projection == null) {
            projection = new String[]{
                    CardEntry._ID,
                    CardEntry.COLUMN_NAME,
                    CardEntry.COLUMN_DESCRIPTON,
                    CardEntry.COLUMN_CODE,
                    CardEntry.COLUMN_COLOR,
                    CardEntry.COLUMN_LOGO,
                    CardEntry.COLUMN_BARCCODE,
            };
        }

        Cursor cursor = db.query(
                CardEntry.TABLE_NAME,   // таблица
                projection,            // столбцы
                selection,                  // столбцы для условия WHERE
                selectionArgs,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        try {

            int idColumnIndex = cursor.getColumnIndex(CardEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(CardEntry.COLUMN_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(CardEntry.COLUMN_DESCRIPTON);
            int codeColumnIndex = cursor.getColumnIndex(CardEntry.COLUMN_CODE);
            int colorColumnIndex = cursor.getColumnIndex(CardEntry.COLUMN_COLOR);
            int logoColumnIndex = cursor.getColumnIndex(CardEntry.COLUMN_LOGO);
            int barccodeColumnIndex = cursor.getColumnIndex(CardEntry.COLUMN_BARCCODE);

            // Проходим через все ряды
            while (cursor.moveToNext()) {
                imageList.add(new Card(context,
                    cursor.getInt(idColumnIndex),
                    cursor.getString(nameColumnIndex),
                    cursor.getString(descriptionColumnIndex),
                    cursor.getString(codeColumnIndex),
                    cursor.getString(colorColumnIndex),
                    cursor.getString(logoColumnIndex),
                    cursor.getString(barccodeColumnIndex)
                ));
            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
        return imageList;
    }

    public static void clearing(Context context) {
        if (db == null) {
            Card.init(context);
        }
        db.delete(CardEntry.TABLE_NAME,  null, null);
    }

    /**
     * Сохранение в базу
     */
    public void save() {
        ContentValues values = new ContentValues();
        values.put(CardEntry.COLUMN_NAME, name);
        values.put(CardEntry.COLUMN_DESCRIPTON, description);
        values.put(CardEntry.COLUMN_CODE, code);
        values.put(CardEntry.COLUMN_COLOR, color);
        values.put(CardEntry.COLUMN_LOGO, logo);
        values.put(CardEntry.COLUMN_BARCCODE, barcode);
        if (id > 0) {
            db.update(CardEntry.TABLE_NAME, values, CardEntry._ID + "= ?", new String[]{Long.toString(id)});
        } else {
            id = db.insert(CardEntry.TABLE_NAME, null, values);
        }
    }

    /**
     * Удаление из базы
     * если не задан id запрос на удаление не отправляется
     * @return
     */
    public int delete() {
        if (id > 0)
            return db.delete(CardEntry.TABLE_NAME,  CardEntry._ID + "= ?", new String[]{Long.toString(id)});
        return -1;
    }

    private static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        Log.i("file",image.getAbsolutePath());
        return image;
    }

    public void setFile(Bitmap thumbnailBitmap, int code){
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File

        }if (photoFile != null) {

            try {
                FileOutputStream out = new FileOutputStream(photoFile.getPath());
                thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                if (out != null) {
                    out.close();
                }
                if (code == REQUEST_TAKE_LOGO) {
                    logo = photoFile.getPath();
                    ColorArt colorArt = new ColorArt(thumbnailBitmap);
                    color = String.valueOf(colorArt.getPrimaryColor());
                }else if (code == REQUEST_TAKE_BARCODE)
                    barcode = photoFile.getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dispatchTakePictureIntent(int code) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mainContext.getPackageManager()) != null) {
                mainContext.startActivityForResult(takePictureIntent, code);
        }
    }

    public static class CardEntry implements BaseColumns {
        public final static String TABLE_NAME = "card";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_DESCRIPTON = "description";
        public final static String COLUMN_CODE = "code";
        public final static String COLUMN_COLOR = "color";
        public final static String COLUMN_LOGO = "logo";
        public final static String COLUMN_BARCCODE = "barcode";

    }

    public static class CardDbHelper  extends SQLiteOpenHelper {

        public static final String LOG_TAG = CardDbHelper.class.getSimpleName();

        private static final String DATABASE_NAME = "cards.db";

        private static final int DATABASE_VERSION = 1;

        public CardDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Строка для создания таблицы
            String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + CardEntry.TABLE_NAME + " ("
                    + CardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CardEntry.COLUMN_NAME + " TEXT NOT NULL, "
                    + CardEntry.COLUMN_DESCRIPTON + " TEXT NOT NULL, "
                    + CardEntry.COLUMN_CODE + " TEXT NOT NULL, "
                    + CardEntry.COLUMN_COLOR + " TEXT NOT NULL, "
                    + CardEntry.COLUMN_LOGO + " TEXT NOT NULL, "
                    + CardEntry.COLUMN_BARCCODE + " TEXT NOT NULL);";

            // Запускаем создание таблицы
            db.execSQL(SQL_CREATE_GUESTS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
