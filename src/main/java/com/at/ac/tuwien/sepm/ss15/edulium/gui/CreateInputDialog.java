package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

public class CreateInputDialog<Domain> extends InputDialog<Domain> {

    private Validator<Domain> validator;

    public CreateInputDialog(String domainName) {
        super(domainName);

        setTitle("Add " + domainName);
        setHeaderText("Add a new " + domainName);
    }

    public void setValidator(Validator<Domain> validator) {
        this.validator = validator;
    }

    @Override
    public void setController(InputDialogController<Domain> controller) {
        controller.prepareForCreate();

        super.setController(controller);
    }

    @Override
    protected void validate(Domain domainObject) throws ValidationException {
        assert validator != null;

        validator.validateForCreate(domainObject);
    }
}