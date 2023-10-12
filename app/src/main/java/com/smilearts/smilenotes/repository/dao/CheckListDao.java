package com.smilearts.smilenotes.repository.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.smilearts.smilenotes.repository.table.CheckListTable;

import java.util.List;

@Dao
public interface CheckListDao {

    @Insert
    void Insert(CheckListTable model);

    @Insert
    void Insert(List<CheckListTable> model);

    @Query("SELECT * FROM CheckList")
    List<CheckListTable> getList();

    @Query("SELECT * FROM CheckList ORDER BY Title DESC")
    List<CheckListTable> getNotesDESC();

    @Query("SELECT * FROM CheckList ORDER BY Title ASC")
    List<CheckListTable> getNotesASC();

    @Query("DELETE FROM CheckList WHERE Id=:Id")
    void Delete(int Id);

    @Query("UPDATE CheckList SET Title =:title , Points =:points ,Remainder =:reminder, Time =:time , Date =:date , Bg =:bg , Priority =:pri WHERE Id =:id")
    void Update(int id , String title , String points , String reminder, String date, String time, String bg, int pri);

    @Query("DELETE FROM CheckList")
    void ClearAll();

}
