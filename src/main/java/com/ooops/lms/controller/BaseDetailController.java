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
    }

    protected abstract void loadItemDetails();

    public void setAddMode(boolean isAdd){
        this.addMode = isAdd;
        updateAddModeUI();
    };

    protected abstract void updateAddModeUI();

    public void setEditMode(boolean enable) {
        this.editMode = enable;
        updateEditModeUI();
    }

    protected abstract void updateEditModeUI();

    public void saveChanges() {
        try {
            if (addMode) {
                if (validateInput() && getNewItemInformation()) {
                    boolean confirmYes = CustomerAlter.showAlter("Thêm sách mới?");
                    if (confirmYes) {
                        Command addCommand = new AdminCommand("add", this.item);
                        commandInvoker.setCommand(addCommand);
                        if (commandInvoker.executeCommand()) {
                            mainController.loadData();
                            setAddMode(false);
                            System.out.println("Đã lưu thay đổi");
                        }
                    }
                }
            } else {
                if (validateInput() && getNewItemInformation()) {
                    boolean confirmYes = CustomerAlter.showAlter("Bạn có muốn lưu thay đổi này không?");
                    if (confirmYes) {
                        // sửa sách trong CSDL
                        Command editCommand = new AdminCommand("edit", this.item);
                        commandInvoker.setCommand(editCommand);
                        if (commandInvoker.executeCommand()) {
                            mainController.loadData();
                            setEditMode(false);
                            System.out.println("Đã lưu thay đổi");
                        }

                    }
                } else {
                    System.out.println("Tiếp tục edit");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    public void loadStartStatus() {
        item = null;
        setEditMode(false);
        setAddMode(false);
    }

    protected abstract boolean validateInput();

    protected abstract boolean getNewItemInformation() throws Exception;


}
