package com.smilearts.smilenotes.repository.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.smilearts.smilenotes.model.NotesModel;
import com.smilearts.smilenotes.model.RecycleModel;

import java.util.List;

@Dao
public interface NotesDao {

    @Insert
    void Insert(NotesModel model);

    @Insert
    void UpdateBackup(List<NotesModel> model);

    @Insert
    void InsertRecycle(RecycleModel model);

    @Query("SELECT * FROM Recycle")
    List<RecycleModel> getRecycleList();

    @Query("DELETE FROM Recycle WHERE Id =:id")
    void DeleteRecycle(int id);

    @Query("DELETE FROM Recycle")
    void DeleteAllRecycle();

    @Query("SELECT * FROM Notes")
    List<NotesModel> getNotes();

    @Query("SELECT * FROM Notes ORDER BY Title DESC")
    List<NotesModel> getNotesDESC();

    @Query("SELECT * FROM Notes ORDER BY Title ASC")
    List<NotesModel> getNotesASC();

    @Query("SELECT * FROM Notes WHERE Id =:Id")
    NotesModel getNote(int Id);

    @Query("UPDATE Notes SET Title =:title , Message =:message , Time =:time , Date =:date , Bg =:bg , Priority =:pri WHERE Id =:id")
    void Update(int id , String title , String message , String time , String date , String bg , int pri);

    @Query("DELETE FROM Notes WHERE Id=:Id")
    void DeleteNote(int Id);

    @Query("DELETE FROM Notes")
    void ClearNotes();

}
