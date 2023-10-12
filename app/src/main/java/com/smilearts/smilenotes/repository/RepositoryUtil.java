package com.smilearts.smilenotes.repository;

import android.app.Activity;
import android.content.Context;
import androidx.lifecycle.MutableLiveData;

import com.smilearts.smilenotes.repository.model.CheckListRep;
import com.smilearts.smilenotes.repository.model.RecycleBinRep;
import com.smilearts.smilenotes.repository.config.RoomDB;
import com.smilearts.smilenotes.repository.model.NotesRepository;
import com.smilearts.smilenotes.util.TempData;

public class RepositoryUtil {

    private Activity activity;
    private Context context;
    public TempData tempData;

    private RoomDB roomDB;

    private NotesRepository notesRep;
    private RecycleBinRep recycleBinRep;
    private CheckListRep checkListRep;

    public MutableLiveData<String> errorStatus = new MutableLiveData<>();

    public RepositoryUtil() {
    }

    public RepositoryUtil(Context context , TempData tempData) {
        this.context = context;
        this.tempData = tempData;
        this.roomDB = RoomDB.getInstance(activity.getApplicationContext());
        this.notesRep = new NotesRepository(this);
        this.recycleBinRep = new RecycleBinRep(this);
        this.checkListRep = new CheckListRep(this);
    }

    public RepositoryUtil(Activity activity, TempData tempData) {
        this.activity = activity;
        this.tempData = tempData;
        this.roomDB = RoomDB.getInstance(activity.getApplicationContext());
        this.notesRep = new NotesRepository(this);
        this.recycleBinRep = new RecycleBinRep(this);
        this.checkListRep = new CheckListRep(this);
    }

    public RoomDB getRoomDB() {
        return roomDB;
    }

    public NotesRepository getNotesRep() {
        return notesRep;
    }

    public RecycleBinRep getRecycleBinRep() {
        return recycleBinRep;
    }

    public CheckListRep getCheckListRep() {
        return checkListRep;
    }
}
