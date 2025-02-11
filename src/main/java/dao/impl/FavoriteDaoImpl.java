package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import commend.ServiceLocator;
import dao.FavoriteDao;
import member.bean.Favorite;
import service.PublishService;

public class FavoriteDaoImpl implements FavoriteDao {
	DataSource dataSource;

	public FavoriteDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	@Override
	public int insert(Favorite favorite) {
	    final String sql = "INSERT INTO FORFUN.favorite (MEMBER_ID, PUBLISH_ID, CREATE_TIME) VALUES (?, ?, ?);";
        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
        ) {
            pstmt.setInt(1, favorite.getMemberId());
            pstmt.setInt(2, favorite.getPublishId());
            pstmt.setTimestamp(3, favorite.getCreateTime());
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return -1;
	}

	@Override
	public int deleteById(int favoriteId) {
		final String sql = "DELETE FROM FORFUN.favorite WHERE FAVORITE_ID = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql);) {
			pstmt.setInt(1, favoriteId);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int update(Favorite favorite) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Favorite selectById(int favoriteId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Favorite> selectByMemberId(int memberId) {
		final String sql = "SELECT * FROM FORFUN.favorite WHERE MEMBER_ID = ? order by CREATE_TIME DESC";
		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, memberId);
			ResultSet rs = pstmt.executeQuery();
			List<Favorite> favorites = new ArrayList<>();
			while (rs.next()) {
				Favorite favorite = new Favorite();
				favorite.setFavoriteId(rs.getInt("FAVORITE_ID"));
				favorite.setMemberId(rs.getInt("MEMBER_ID"));
				favorite.setPublishId(rs.getInt("PUBLISH_ID"));
				favorite.setCreateTime(rs.getTimestamp("CREATE_TIME"));
				// 刊登單狀態為開啟的
				if (new PublishService().selectById(favorite.getPublishId()).getStatus() == 3) {
						favorites.add(favorite);
					}

			}
			return favorites;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    @Override
    public Favorite selectByMemberIdAndPublishId(int memberId, int publishId) {
        final String sql = "SELECT * FROM FORFUN.favorite WHERE MEMBER_ID = ? and PUBLISH_ID = ?;";
        
        try (
            Connection conn = dataSource.getConnection(); 
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, publishId);
            
            try (
                ResultSet rs = pstmt.executeQuery();
            ) {
                if(rs.next()) {
                    Favorite favorite = new Favorite();
                    favorite.setFavoriteId(rs.getInt("FAVORITE_ID"));
                    favorite.setMemberId(rs.getInt("MEMBER_ID"));
                    favorite.setPublishId(rs.getInt("PUBLISH_ID"));
                    favorite.setCreateTime(rs.getTimestamp("CREATE_TIME"));
                    
                    return favorite;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public int deleteByPublishId(int publishId) {
		final String sql = "DELETE FROM FORFUN.favorite WHERE PUBLISH_ID = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql);) {
			pstmt.setInt(1, publishId);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
