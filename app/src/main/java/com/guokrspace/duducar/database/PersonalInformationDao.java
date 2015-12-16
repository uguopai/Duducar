package com.guokrspace.duducar.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.guokrspace.duducar.database.PersonalInformation;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PERSONAL_INFORMATION.
*/
public class PersonalInformationDao extends AbstractDao<PersonalInformation, Long> {

    public static final String TABLENAME = "PERSONAL_INFORMATION";

    /**
     * Properties of entity PersonalInformation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Token = new Property(1, String.class, "token", false, "TOKEN");
        public final static Property Mobile = new Property(2, String.class, "mobile", false, "MOBILE");
    };


    public PersonalInformationDao(DaoConfig config) {
        super(config);
    }
    
    public PersonalInformationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PERSONAL_INFORMATION' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'TOKEN' TEXT," + // 1: token
                "'MOBILE' TEXT);"); // 2: mobile
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PERSONAL_INFORMATION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PersonalInformation entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String token = entity.getToken();
        if (token != null) {
            stmt.bindString(2, token);
        }
 
        String mobile = entity.getMobile();
        if (mobile != null) {
            stmt.bindString(3, mobile);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PersonalInformation readEntity(Cursor cursor, int offset) {
        PersonalInformation entity = new PersonalInformation( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // token
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // mobile
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PersonalInformation entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setToken(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMobile(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PersonalInformation entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PersonalInformation entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
