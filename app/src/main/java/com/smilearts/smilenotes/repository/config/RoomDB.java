package com.smilearts.smilenotes.repository.config;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.smilearts.smilenotes.repository.dao.CheckListDao;
import com.smilearts.smilenotes.repository.dao.NotesDao;
import com.smilearts.smilenotes.repository.table.CheckListTable;
import com.smilearts.smilenotes.model.NotesModel;
import com.smilearts.smilenotes.model.RecycleModel;
import com.smilearts.smilenotes.util.AppUtil;

@Database(entities = {NotesModel.class , RecycleModel.class, CheckListTable.class} , version = 4 , exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    public abstract NotesDao notesDao();
    public abstract CheckListDao checkListDao();
    public static RoomDB INSTANCE;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            /**
             * No changes
             */
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            /**
             * No changes
             */
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE CheckList (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "Title TEXT," +
                    "Points TEXT," +
                    "Remainder TEXT," +
                    "Date TEXT," +
                    "Time TEXT," +
                    "Bg TEXT," +
                    "Priority INTEGER NOT NULL)");
        }
    };

    public static synchronized RoomDB getInstance(Context mContext){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(mContext , RoomDB.class , AppUtil.DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4)
                    .build();
        }
        return INSTANCE;
    }

}
