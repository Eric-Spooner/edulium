package com.at.ac.tuwien.sepm.ss15.edulium.gui;

public class UpdateInputDialog<Domain> extends InputDialog<Domain> {

    private Domain domainObject;

    public UpdateInputDialog(String domainName, Domain domainObject) {
        super(domainName);

        this.domainObject = domainObject;

        setTitle("Change " + domainName);
        setHeaderText("Change an existing " + domainName);
    }

    @Override
    public void setController(InputDialogController<Domain> controller) {
        controller.prepareForUpdate(domainObject);

        super.setController(controller);
    }
}