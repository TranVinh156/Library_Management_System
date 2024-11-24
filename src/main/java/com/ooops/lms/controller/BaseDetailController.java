package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.AdminCommand;
import com.ooops.lms.Command.Command;

public abstract class BaseDetailController<T> extends BasicController {
    protected T item;
    protected boolean editMode;
    protected boolean addMode;
    protected boolean hasUnsavedChanges;

    protected BasePageController mainController;

    /**
     * Set the main controller
     */
    public void setMainController(BasePageController controller) {
        this.mainController = controller;
    }

    public void setItem(T item) {
        this.item = item;
        loadItemDetails();
        mainController.setTitlePage();
    }

    protected abstract void loadItemDetails();

    /**
     * Set AddMode cho Detail.
     *
     * @param isAdd
     */
    public void setAddMode(boolean isAdd) {
        this.addMode = isAdd;
        updateAddModeUI();
        mainController.setTitlePage();
    }

    protected abstract void updateAddModeUI();

    /**
     * Set EditMode cho Detail.
     *
     * @param enable
     */
    public void setEditMode(boolean enable) {
        this.editMode = enable;
        updateEditModeUI();
        mainController.setTitlePage();
    }

    protected abstract void updateEditModeUI();

    /**
     * Vinh cc.
     */
    protected void saveChanges() {
        try {
            if (addMode) {
                if (validateInput() && getNewItemInformation()) {
                    boolean confirmYes = CustomerAlter.showAlter("Thêm " + getType() + " mới?");
                    if (confirmYes) {
                        Command addCommand = new AdminCommand("add", this.item);
                        commandInvoker.setCommand(addCommand);
                        if (commandInvoker.executeCommand()) {
                            getTitlePageStack().pop();
                            mainController.loadData();
                            setAddMode(false);
                            System.out.println("Đã lưu thay đổi");
                        }
                    }
                }
            } else {
                if (validateInput() && getNewItemInformation()) {
                    boolean confirmYes = CustomerAlter.showAlter("Bạn có muốn lưu thay đổi " + getType() + " này hay không?");
                    if (confirmYes) {
                        // sửa sách trong CSDL
                        Command editCommand = new AdminCommand("edit", this.item);
                        commandInvoker.setCommand(editCommand);
                        if (commandInvoker.executeCommand()) {
                            getTitlePageStack().pop();
                            mainController.loadData();
                            loadItemDetails();
                            setEditMode(false);
                            System.out.println("Đã lưu thay đổi");
                        }

                    }
                } else {
                    System.out.println("Tiếp tục edit");
                }
            }
        } catch (Exception e) {
            CustomerAlter.showMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Khi người dùng bấm nút delete thì gọi command xóa item.
     */
    protected void deleteChanges() {
        try {
            boolean confirmYes = CustomerAlter.showAlter("Bạn có chắc muốn xóa " + getType() + " này chứ?");
            if (confirmYes) {
                Command deleteCommand = new AdminCommand("delete", this.item);
                commandInvoker.setCommand(deleteCommand);
                if (commandInvoker.executeCommand()) {
                    getTitlePageStack().pop();
                    getTitlePageStack().pop();
                    mainController.loadData();
                    mainController.alterPage();
                    setEditMode(false);
                    System.out.println("Đã lưu thay đổi");
                }
            }
        } catch (Exception e) {
            CustomerAlter.showMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    /**
     * load về trạng thái mặc định cho detail.
     */
    public void loadStartStatus() {
        item = null;
        setEditMode(false);
        setAddMode(false);
    }

    /**
     * Kiểm tra xem các thông tin người dùng nhập hợp lệ hay ko.
     *
     * @return
     */
    protected abstract boolean validateInput();

    /**
     * Tạo newItem từ các trường text.
     *
     * @return
     * @throws Exception
     */
    protected abstract boolean getNewItemInformation() throws Exception;

    /**
     * Lấy thể loại của trang nay là gì (Đơn muon sách, Đơn đặt sách,...)
     *
     * @return
     */
    protected abstract String getType();


}
