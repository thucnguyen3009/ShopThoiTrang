//* Coder: NGUYEN DOAN CHI THUC
//* Date of writing code: 29/11/2021
//* MSSV: PC01573
//* Class: IT16301
package com.software.dao;

import com.software.entity.ChucVu;
import com.software.entity.DonViTinh;
import com.software.jdbcHelper.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DonViTinhDAO extends SoftwareDAO<DonViTinh, String> {

    String INSERT_SQL = "INSERT INTO DonViTinh (MaDonVi, TenDonVi, MoTa) VALUES (?, ?, ?)";
    String UPDATE_SQL = "UPDATE DonViTinh SET TenDonVi = ?, MoTa = ? WHERE MaDonVi = ?";
    String DELETE_SQL = "DELETE FROM DonViTinh WHERE MaDonVi = ?";
    String SELECT_ALL_SQL = "SELECT * FROM DonViTinh";
    String SELECT_BY_ID_SQL = "SELECT * FROM DonViTinh WHERE MaDonVi = ?";

    @Override
    public void insert(DonViTinh entity) {
        try {
            XJdbc.update(INSERT_SQL, entity.getMaDonVi(),
                    entity.getTenDonVi(),
                    entity.getMoTa());
        } catch (SQLException ex) {
        }
    }

    @Override
    public void update(DonViTinh entity) {
        try {
            XJdbc.update(UPDATE_SQL, entity.getTenDonVi(),
                    entity.getMoTa(),
                    entity.getMaDonVi());
        } catch (SQLException ex) {
        }
    }

    @Override
    public void delete(String ID) {
        try {
            XJdbc.update(DELETE_SQL, ID);
        } catch (SQLException ex) {
        }
    }

    @Override
    public DonViTinh SelectByID(String ID) {
        List<DonViTinh> list = this.SelectBySQL(SELECT_BY_ID_SQL, ID);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<DonViTinh> SelectAll() {
        return this.SelectBySQL(SELECT_ALL_SQL);
    }

    @Override
    public List<DonViTinh> SelectBySQL(String sql, Object... args) {
        List<DonViTinh> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {
                DonViTinh donVi = new DonViTinh();
                donVi.setMaDonVi(rs.getString("MaDonVi"));
                donVi.setTenDonVi(rs.getString("TenDonVi"));
                donVi.setMoTa(rs.getString("MoTa"));
                list.add(donVi);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DonViTinh> selectByKeyWord(String keyword) {
        String sql = "SELECT * FROM DonViTinh WHERE MaDonVi LIKE ? OR TenDonVi LIKE ?";
        return SelectBySQL(sql, "%" + keyword + "%", "%" + keyword + "%");
    }
    
    public List<DonViTinh> selectByDonViInSanPham(String maDVT) {
        String sql = "SELECT * FROM DonViTinh WHERE MaDonVi = ? and MaDonVi in (select DonViTinh from SanPham)";
        return this.SelectBySQL(sql, maDVT);
    }
    
    //Check
    public String CheckThemDonViTinh(String maDVT, String tenDVT, String moTa) {
    	DonViTinh dvt = new DonViTinh(maDVT, tenDVT, moTa);
    	DonViTinh list = SelectByID(maDVT);
    	if (!maDVT.isEmpty() || !tenDVT.isEmpty()) {
			if (list == null) {
				insert(dvt);
				list = SelectByID(maDVT);
				if(list != null) {
					return "Thêm thành công";
				}
			}
		}
		return "Thêm thất bại";
    }
    //
	public String checkSuaDonViTinh(String maDVT, String tenDVT, String moTa) {
		DonViTinh dvt = new DonViTinh(maDVT, tenDVT, moTa);
    	DonViTinh list = SelectByID(maDVT);

		if (!maDVT.isEmpty() || !tenDVT.isEmpty()) {
			if (list != null) {
				update(dvt);
				if(!tenDVT.equalsIgnoreCase(list.getTenDonVi()) && !moTa.equalsIgnoreCase(list.getMoTa())) {
					return "Sửa thành công";
				}else {
					return "Dữ liệu chưa thay đổi";
				}
			}
		}
		return "Sửa thất bại";
	}
	//
	public String checkXoaDonViTinh(String maDVT, boolean luaChon) {
		DonViTinh list = SelectByID(maDVT);
			if (list != null) {
				if(luaChon == true) {
					delete(maDVT);
					list = SelectByID(maDVT);
					if(list == null) {
						return "Xóa thành công";
					}
				}else {
					return "Dữ liệu chưa được xóa";
				}
			}
		return "Xóa thất bại";
	}
	//
	public String checkTimDonViTinh(String maDVT ) {
		DonViTinh list = SelectByID(maDVT);
		if(list != null) {
			return "Tìm thành công";
		}
		return "Đơn vị tính không tồn tại";
	}
    
}
