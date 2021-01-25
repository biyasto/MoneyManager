package com.example.assignment.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.assignment.database.Database;
import com.example.assignment.model.GiaoDich;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DaoGiaoDich {
    Database dtb;
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    SQLiteDatabase dtGD;

    public DaoGiaoDich(Context context) {
        dtb = new Database(context);

    }


    public ArrayList<GiaoDich> getGD(String sql, String... a) {
        ArrayList<GiaoDich> list = new ArrayList<>();
        dtGD = dtb.getReadableDatabase();
        Cursor cs = dtGD.rawQuery(sql, a);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            try {
                int ma = Integer.parseInt(cs.getString(0));
                String mota = cs.getString(1);
                String ngay = cs.getString(2);
                int tien = Integer.parseInt(cs.getString(3));
                int maK = Integer.parseInt(cs.getString(4));
                GiaoDich gd = new GiaoDich(ma, mota, dfm.parse(ngay), tien, maK);
                list.add(gd);
                cs.moveToNext();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cs.close();
        return list;
    }


    public ArrayList<GiaoDich> getAll() {
        String sql = "SELECT * FROM GIAODICH";
        return getGD(sql);
    }

    //Lấy giao dịch theo loại
    public ArrayList<GiaoDich> getGDtheoTC(int loaiKhoan) {
        String sql = "SELECT * FROM GIAODICH as gd INNER JOIN THUCHI as tc ON gd.maKhoan = tc.maKhoan WHERE tc.loaiKhoan=?";
        ArrayList<GiaoDich> list = getGD(sql, String.valueOf(loaiKhoan));
        return list;
    }

    public boolean themGD(GiaoDich gd) {
        dtGD = dtb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("moTaGd", gd.getMoTaGd());
        contentValues.put("ngayGd", dfm.format(gd.getNgayGd()));
        contentValues.put("soTien", gd.getSoTien());
        contentValues.put("maKhoan", gd.getMaKhoan());
        long r = dtGD.insert("GIAODICH", null, contentValues);
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Sửa giao dịch theo mã giao dịch
    public boolean suaGD(GiaoDich gd) {
        SQLiteDatabase db = dtb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("moTaGd", gd.getMoTaGd());
        contentValues.put("ngayGd", dfm.format(gd.getNgayGd()));
        contentValues.put("soTien", gd.getSoTien());
        contentValues.put("maKhoan", gd.getMaKhoan());
        int r = db.update("GIAODICH", contentValues, "maGd=?", new String[]{String.valueOf(gd.getMaGd())});
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Xóa giao dịch theo mã
    public boolean xoaGD(GiaoDich gd) {
        SQLiteDatabase db = dtb.getWritableDatabase();
        int r = db.delete("GIAODICH", "maGd=?", new String[]{String.valueOf(gd.getMaGd())});
        if (r <= 0) {
            return false;
        }
        return true;
    }


}
