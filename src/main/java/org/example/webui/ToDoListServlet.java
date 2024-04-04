package org.example.webui;

import entity.TestItems;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@WebServlet(name="org.example.webui.ToDoListServlet", value="/To-Do-List-Servlet")
public class ToDoListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("item_name");
        String description = request.getParameter("item_description");
        Integer priority = null;

        String priorityParameter = request.getParameter("item_priority");
        if (priorityParameter != null && !priorityParameter.isEmpty()) {
            try {
                priority = Integer.valueOf(priorityParameter);
            } catch (NumberFormatException e) {
                // Handle parsing error if needed
                e.printStackTrace();
            }
        }

        // Insert data into the database
        insertItemIntoDatabase(name, description, priority);

        // Redirect the user to result.jsp
        response.sendRedirect("result.jsp");
    }

    private void insertItemIntoDatabase(String name, String description, Integer priority) {
        String url = "jdbc:mysql://localhost:3306/Todo";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // WARNING: Vulnerable to SQL injection
            String sql = "INSERT INTO items (title, description, priority) VALUES ('" + name + "', '" + description + "', '" + priority + "')";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            // Handle SQL exception
            e.printStackTrace();
        }
    }
}
