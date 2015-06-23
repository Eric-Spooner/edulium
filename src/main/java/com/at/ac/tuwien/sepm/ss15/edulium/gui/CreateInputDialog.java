package com.at.ac.tuwien.sepm.ss15.edulium.gui;

public class CreateInputDialog<Domain> extends InputDialog<Domain> {

    public CreateInputDialog(String domainName) {
        super(domainName);

        setTitle("Add " + domainName);
        setHeaderText("Add a new " + domainName);
    }

    @Override
    public void setController(InputDialogController<Domain> controller) {
        controller.prepareForCreate();

        super.setController(controller);
    }
}