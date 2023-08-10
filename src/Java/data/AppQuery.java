package Java.data;

import Java.model.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AppQuery {
    private DBConnection db = new DBConnection();
    public void addTeacher(Teacher teacher){
        try {
            db.getDBConnection();
            PreparedStatement preparedStatement = db.getConnection().prepareStatement("Insert into teacher(teacherId, fullName, position, sex, classSupervisor, teachingSchedule, contractStartDate,contractEndDate, salary, address)  " +
                    "values (?, ?, ?, ?, ? , ? , ? , ? , ? , ? )");
            preparedStatement.setInt(1,teacher.getTeacherId());
            preparedStatement.setString(2,teacher.getFullName());
            preparedStatement.setString(3,teacher.getPosition());
            preparedStatement.setString(4,teacher.getSex());
            preparedStatement.setString(5,teacher.getClassSupervisor());
            preparedStatement.setString(6,teacher.getTeacherSchedule());
            preparedStatement.setString(7, teacher.getContractStartDate());
            preparedStatement.setString(8, teacher.getContractEndDate());

            preparedStatement.setFloat(9,teacher.getSalary());
            preparedStatement.setString(10,teacher.getAddress());
            preparedStatement.execute();
            preparedStatement.close();
            db.closeConnection();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ObservableList<Teacher> getTeacherList() {
            ObservableList <Teacher> teacherList = FXCollections.observableArrayList();
            try {
                String query  =  "Select * from teacher order by fullName asc ";
                db.getDBConnection();
                Statement statement = db.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                Teacher teacher;
                while (resultSet.next()){
                    teacher = new Teacher();
                    teacher.setTeacherId(resultSet.getInt("teacherId"));
                    teacher.setFullName(resultSet.getString("fullName"));
                    teacher.setPosition(resultSet.getString("position"));
                    teacher.setSex(resultSet.getString("sex"));
                    teacher.setClassSupervisor(resultSet.getString("classSupervisor"));
                    teacher.setTeacherSchedule(resultSet.getString("teachingSchedule"));
                    teacher.setContractStartDate(resultSet.getString("contractStartDate"));
                    teacher.setContractEndDate(resultSet.getString("contractEndDate"));
                    teacher.setSalary(resultSet.getInt("salary"));
                    teacher.setAddress(resultSet.getString("address"));

                    teacherList.add(teacher);
                }
                resultSet.close();
                db.closeConnection();
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return teacherList;
    }
    public void deleteData(Teacher teacher){
        try {
            db.getDBConnection();
            String query = "Delete from teacher " +
                    " Where teacherId = ? ";

            PreparedStatement preparedStatement = db.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,teacher.getTeacherId());
            preparedStatement.execute();
            db.closeConnection();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateData(Teacher teacher){
        try {
            db.getDBConnection();
            PreparedStatement preparedStatement = db.getConnection().prepareStatement("UPDATE teacher SET fullName = ?, position = ?, sex = ?, classSupervisor = ?, teachingSchedule = ?, " +
                    "contractStartDate = ?, contractEndDate = ?, salary = ?, address = ? WHERE teacherId = ?");

            preparedStatement.setString(1,teacher.getFullName());
            preparedStatement.setString(2,teacher.getPosition());
            preparedStatement.setString(3,teacher.getSex());
            preparedStatement.setString(4,teacher.getClassSupervisor());
            preparedStatement.setString(5,teacher.getTeacherSchedule());
            preparedStatement.setString(6,teacher.getContractStartDate());
            preparedStatement.setString(7,teacher.getContractEndDate());
            preparedStatement.setFloat(8,teacher.getSalary());
            preparedStatement.setString(9,teacher.getAddress());
            preparedStatement.setInt(10,teacher.getTeacherId());

            preparedStatement.execute();
            db.closeConnection();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
