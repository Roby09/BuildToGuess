package me.quin.buildtoguess.profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProfileLoader extends BukkitRunnable {

    private Profile profile;

    private static final String INSERT = "INSERT INTO ze_players VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE name=?";
    private static final String SELECT = "SELECT coins FROM ze_players WHERE uuid=?";
    
    public ProfileLoader()
    {
    }

    @Override
    public void run() 
    {
        Connection connection = null;

        try 
        {
            connection = profile.getBuildToGuess().getDatabas().getHikari().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setString(1, profile.getUuid().toString());
            preparedStatement.setString(2, profile.getName());
            preparedStatement.setInt(3, 0);
            preparedStatement.execute();

            preparedStatement = connection.prepareStatement(SELECT);
            preparedStatement.setString(1, profile.getUuid().toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) 
            {
                profile.setCoins(resultSet.getInt("coins"));
            }

            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) 
        {
            e.printStackTrace();
        } finally {
            if (connection != null) 
            {
                try {
                    connection.close();
                } catch (SQLException e) 
                {
                    e.printStackTrace();
                }
            }
        }
    }

}