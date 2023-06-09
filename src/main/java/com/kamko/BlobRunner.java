package com.kamko;

import com.kamko.util.ConnectionManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.sql.*;

public class BlobRunner {
    public static void main(String[] args) throws SQLException, IOException {
        getImage();
    }

    private static void saveImage() throws SQLException, IOException {
        String sql = """
                UPDATE aircraft
                SET image = ?
                WHERE id = 1;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);

            Path path = Paths.get("C:\\Users\\Roman\\IdeaProjects\\jdbc-starter\\src\\main\\resources\\boing777.jpg");
            Blob blob = connection.createBlob();
            blob.setBytes(1, Files.readAllBytes(path));

            preparedStatement.setBlob(1, blob);
            preparedStatement.executeUpdate();

            connection.commit();
        }
    }

    private static void getImage() {
        String sql = """
                SELECT image
                FROM aircraft
                WHERE id = ?;
                """;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                byte[] images = resultSet.getBytes("image");
                Path path = Path.of("C:\\Users\\Roman\\IdeaProjects\\jdbc-starter\\src\\main\\resources\\boing777new.jpg");
                Files.write(path, images, StandardOpenOption.CREATE);
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
