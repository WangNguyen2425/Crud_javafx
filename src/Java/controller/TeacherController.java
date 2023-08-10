package Java.controller;

import Java.data.AppQuery;
import Java.model.Teacher;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        showTeacher();
        searchTf.textProperty().addListener((ObservableList, oldValue,newValue) ->{
            filterData(newValue);
        });
        searchWithClass.textProperty().addListener((ObservableList, oldValue,newValue) ->{
            filterDataWithClass(newValue);
        });

        //  add data vào choice box
        ObservableList<String> classChoices = FXCollections.observableArrayList("1A", "1B", "1C","1D","1E","2A", "2B", "2C","2D","2E","3A", "3B", "3C","3D","3E","4A", "4B", "4C","4D","4E","5A", "5B", "5C","5D","5E");
        classChoicebox.getItems().addAll(classChoices);
        // lấy data từ choice box chọn lớp chủ nhiệm
        classChoicebox.setOnAction(event -> {
            String selectedClass = classChoicebox.getValue();
            System.out.println(selectedClass);

        });


    }
    @FXML
    public TextField searchTf;
    @FXML
    public TextField searchWithClass;
    @FXML
    public TextField teacherId_tf, fullName_tf, rank_tf, sex_tf, schedule_tf, startDate_tf, endDate_tf, salary_tf, address_tf;
    @FXML
    public ChoiceBox<String> classChoicebox;
    @FXML
    public Button btnNew;
    @FXML
    public Button btnSave;
    @FXML
    public Button btnUpdate;
    @FXML
    public Button btnDelete;
    @FXML
    public TableView<Teacher> teacherTableView;
    @FXML
    public TableColumn<Teacher,Integer> colStt;

    @FXML
    public TableColumn<Teacher,String> colFullName,colClassSup,colStartDate, colEndDate, colAddress;
    @FXML
    public TableColumn<Teacher,Integer> colTeacherId;

    private Teacher currentTeacherSelected;



    @FXML
    private void addTeacher(){

       if(!validateData()){
            return;
        }
        Teacher teacher = new Teacher();

        // kiểm tra đảm bảo rằng teacherId này chưa tồn tại
        AppQuery query = new AppQuery();
        ObservableList<Teacher> list = query.getTeacherList();
        for(Teacher t : list){
            if(Objects.equals(t.getTeacherId(), Integer.valueOf(teacherId_tf.getText()))){
                showToolTip("Mã giáo viên này đã gán cho: " + t.getFullName() + " vui lòng nhập lại. ");
                return;
            }
            else {
                teacher.setTeacherId(Integer.valueOf(teacherId_tf.getText()));

            }
        }

        teacher.setFullName(fullName_tf.getText());
        teacher.setPosition(rank_tf.getText());

        // lấy dữ liệu từ choice box
        String selectedValue = classChoicebox.getValue();
        System.out.println("Selected value: " + selectedValue);

        // kiểm tra xem lớp đó đã có giáo viên nào chủ nghiệm hay chưa nếu có rồi thì hiển thị lên thông báo hỏi xem người dùng có thực sự muốn đổi hay không
        for(Teacher t2 : list){
            if(t2.getClassSupervisor().equals(selectedValue)){
                Dialog<ButtonType> dialog2 = new Dialog<>();
                dialog2.setTitle("Xác nhận thay đổi giáo viên chủ nhiệm");
                dialog2.setHeaderText("Lớp " + selectedValue + " đã có giáo viên chủ nhiệm là thầy/cô: " + t2.getFullName() + " chủ nhiệm rồi. Bạn có chắc chắn muốn thay đổi ?");
                dialog2.initModality(Modality.APPLICATION_MODAL); // chế độ hiển thị hộp thoại có thể đóng

                ButtonType okBtn = new ButtonType("Thay đổi",ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelBtn = new ButtonType("Hủy",ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog2.getDialogPane().getButtonTypes().addAll(okBtn,cancelBtn);

                Optional<ButtonType> result = dialog2.showAndWait();

                if(result.isPresent() && result.get() == okBtn){
                    try {
                        teacher.setClassSupervisor(selectedValue);
                        // cập nhật lại cho giáo viên mà đã phụ trách lớp trước đó
                        t2.setClassSupervisor("");
                        query.updateData(t2);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    return;
                }

            }
            else {
                teacher.setClassSupervisor(selectedValue);

            }
        }


        teacher.setSex(sex_tf.getText());
        teacher.setTeacherSchedule(schedule_tf.getText());;
        teacher.setContractStartDate(startDate_tf.getText());
        teacher.setContractEndDate(endDate_tf.getText());
        teacher.setSalary(Integer.parseInt(salary_tf.getText()));
        teacher.setAddress(address_tf.getText());

        query.addTeacher(teacher);

        clearData();

        showTeacher();

        showToolTip("Lưu thành công");
        // reset ChoiceBox về trạng thái không có menu nào được chọn
        classChoicebox.getSelectionModel().clearSelection();


    }
    @FXML
    private void showTeacher(){
        AppQuery query = new AppQuery();
        ObservableList<Teacher> list = query.getTeacherList();


        // Thêm một cột đánh số STT (từ 1 đến số lượng của danh sách)
        colStt.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null) {
                    setText(null);
                } else {
                    int rowIndex = getTableRow().getIndex();
                    setText(String.valueOf(rowIndex + 1));
                }
            }
        });

        colFullName.setCellValueFactory(new PropertyValueFactory<Teacher,String>("fullName"));
        colTeacherId.setCellValueFactory(new PropertyValueFactory<Teacher,Integer>("teacherId"));
        colClassSup.setCellValueFactory(new PropertyValueFactory<Teacher,String>("classSupervisor"));

        // căn giữa cho cột chủ nghiệm lớp
        colClassSup.setCellFactory(column -> {
            return new TableCell<Teacher, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setAlignment(Pos.CENTER); // Căn giữa nếu dữ liệu rỗng hoặc null
                    } else {
                        setText(item);
                        setAlignment(Pos.CENTER); // Căn giữa với dữ liệu không rỗng
                    }
                }
            };
        });

        colStartDate.setCellValueFactory(new PropertyValueFactory<Teacher,String>("contractStartDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<Teacher,String>("contractEndDate"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Teacher,String>("address"));

        teacherTableView.setItems(list);

    }

    @FXML
    private void clickItem(MouseEvent event) {
        try {
            Teacher t = teacherTableView.getSelectionModel().getSelectedItem();
            if (t != null) {
                t = new Teacher(t.getTeacherId(), t.getFullName(), t.getPosition(), t.getClassSupervisor(),t.getTeacherSchedule(),t.getSex(),t.getAddress(),t.getContractStartDate().toString(),t.getContractEndDate().toString(),t.getSalary());
                this.currentTeacherSelected = t;
                teacherId_tf.setDisable(true);
                teacherId_tf.setText(String.valueOf(currentTeacherSelected.getTeacherId()));
                fullName_tf.setText(currentTeacherSelected.getFullName());
                rank_tf.setText(currentTeacherSelected.getPosition());
                sex_tf.setText(currentTeacherSelected.getSex());
                classChoicebox.setValue(currentTeacherSelected.getClassSupervisor());
                schedule_tf.setText(currentTeacherSelected.getTeacherSchedule());
                startDate_tf.setText(currentTeacherSelected.getContractStartDate().toString());
                endDate_tf.setText(currentTeacherSelected.getContractEndDate().toString());
                salary_tf.setText(String.valueOf(currentTeacherSelected.getSalary()));
                address_tf.setText(currentTeacherSelected.getAddress());

                btnDelete.setDisable(false);
                btnUpdate.setDisable(false);
                btnSave.setDisable(true);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void btnNewClick(){
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        teacherId_tf.setDisable(false);
        clearData();

    }
    @FXML
    public void btnDeleteClick(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Xác nhận xóa");
        dialog.setHeaderText("Bạn chắc chắn muốn xóa hồ sơ này chứ ?");
        dialog.initModality(Modality.APPLICATION_MODAL); // chế độ hiển thị hộp thoại có thể đóng
        Label label = new Label("Tên:  " + fullName_tf.getText() + " / Mã giáo viên: " + teacherId_tf.getText());
        label.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        dialog.getDialogPane().setContent(label);
        ButtonType okBtn = new ButtonType("Xóa",ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Hủy",ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn,cancelBtn);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == okBtn){
            try {
                AppQuery appQuery = new AppQuery();
                appQuery.deleteData(currentTeacherSelected);
                showTeacher();
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
                btnNewClick();

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        showToolTip("Xóa thành công");
    }
    @FXML
    public void btnUpdateClick( ){
        try {
            AppQuery query = new AppQuery();
            ObservableList<Teacher> list = query.getTeacherList();
            AppQuery appQuery = new AppQuery();
            Teacher teacherUpdate = new Teacher();
            teacherUpdate.setTeacherSchedule(schedule_tf.getText());;
            teacherUpdate.setContractStartDate(startDate_tf.getText());
            teacherUpdate.setContractEndDate(endDate_tf.getText());
            teacherUpdate.setSalary(Integer.parseInt(salary_tf.getText()));
            teacherUpdate.setAddress(address_tf.getText());
            teacherUpdate.setTeacherId(Integer.valueOf(teacherId_tf.getText()));
            teacherUpdate.setFullName(fullName_tf.getText());
            teacherUpdate.setPosition(rank_tf.getText());
            teacherUpdate.setSex(sex_tf.getText());

            // lấy dữ liệu từ choice box
            String selectedValueUpdate = classChoicebox.getValue();
            System.out.println("Selected value: " + selectedValueUpdate);

            // kiểm tra xem lớp đó đã có giáo viên nào chủ nghiệm hay chưa nếu có rồi thì hiển thị lên thông báo hỏi xem người dùng có thực sự muốn đổi hay không
            for(Teacher t2 : list){
                if(t2.getClassSupervisor().equals(selectedValueUpdate)){
                    Dialog<ButtonType> dialog2 = new Dialog<>();
                    dialog2.setTitle("Xác nhận thay đổi giáo viên chủ nhiệm");
                    dialog2.setHeaderText("Lớp " + selectedValueUpdate + " đã có giáo viên chủ nhiệm là thầy/cô: " + t2.getFullName() + " chủ nhiệm rồi. Bạn có chắc chắn muốn thay đổi ?");
                    dialog2.initModality(Modality.APPLICATION_MODAL); // chế độ hiển thị hộp thoại có thể đóng

                    ButtonType okBtn = new ButtonType("Thay đổi",ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancelBtn = new ButtonType("Hủy",ButtonBar.ButtonData.CANCEL_CLOSE);
                    dialog2.getDialogPane().getButtonTypes().addAll(okBtn,cancelBtn);

                    Optional<ButtonType> result = dialog2.showAndWait();

                    if(result.isPresent() && result.get() == okBtn){
                        try {
                            teacherUpdate.setClassSupervisor(selectedValueUpdate);
                            // cập nhật lại cho giáo viên mà đã phụ trách lớp trước đó
                            t2.setClassSupervisor("");
                            query.updateData(t2);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else {
                        return;
                    }

                }
                else {
                    teacherUpdate.setClassSupervisor(selectedValueUpdate);

                }
            }

            appQuery.updateData(teacherUpdate);

            showTeacher();
            btnDelete.setDisable(false);
            btnUpdate.setDisable(false);
            btnSave.setDisable(true);
            showToolTip("Cập nhật thông tin thành công!");
        }
        catch (Exception e){
            e.printStackTrace();
            showToolTip("Cập nhật thông tin thất bại");


        }
    }
    public boolean validateData(){
        return !teacherId_tf.getText().equals("") || !fullName_tf.getText().equals("") || !rank_tf.getText().equals("") || !schedule_tf.getText().equals("")|| !startDate_tf.getText().equals("")|| !endDate_tf.getText().equals("")  || !salary_tf.getText().equals("")|| !address_tf.getText().equals("") ;
    }
    public void filterData(String searchName){
        ObservableList<Teacher> filterData = FXCollections.observableArrayList();
        AppQuery appQuery = new AppQuery();
        ObservableList<Teacher> list = appQuery.getTeacherList();
        for(Teacher t : list ){
            if(t.getFullName().toLowerCase().contains(searchName.toLowerCase())
                    ||  t.getTeacherId().toString().toLowerCase().contains(searchName.toLowerCase())){
                filterData.add(t);
            }
            teacherTableView.setItems(filterData);
        }
    }
    public void filterDataWithClass(String className){
        ObservableList<Teacher> filterDataWithClass = FXCollections.observableArrayList();
        AppQuery appQuery = new AppQuery();
        ObservableList<Teacher> list = appQuery.getTeacherList();
        for(Teacher t : list ){
            if(t.getClassSupervisor().toLowerCase().contains(className.toLowerCase())){
                filterDataWithClass.add(t);
            }
            teacherTableView.setItems(filterDataWithClass);
        }
    }

    // reset tf
    private void clearData(){
        teacherId_tf.setText("");
        fullName_tf.setText("");
        rank_tf.setText("");
        sex_tf.setText("");
        schedule_tf.setText("");
        startDate_tf.setText("");
        endDate_tf.setText("");
        salary_tf.setText("");
        address_tf.setText("");
        teacherId_tf.requestFocus();
    }

    private void showToolTip(String message){
        Font font = new Font(16); // font cho tool tip
        Bounds bounds = teacherId_tf.localToScreen(teacherId_tf.getBoundsInLocal());
        double tooltipX = bounds.getMinX() + teacherId_tf.getWidth() / 2;
        double tooltipY = bounds.getMinY() - 40; // Điều chỉnh độ cao theo ý muốn

        Tooltip tooltip = new Tooltip(message);
        tooltip.setFont(font);
        tooltip.show(teacherId_tf.getScene().getWindow(), tooltipX, tooltipY);

        // Tự động đóng tooltip sau một khoảng thời gian
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> tooltip.hide());
        delay.play();
    }

}
