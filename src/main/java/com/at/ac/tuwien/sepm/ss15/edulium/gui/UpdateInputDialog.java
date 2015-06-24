package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;
import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.Validator;

public class UpdateInputDialog<Domain> extends InputDialog<Domain> {

    private final Domain domainObject;
    private Validator<Domain> validator;

    public UpdateInputDialog(String domainName, Domain domainObject) {
        super(domainName);

        this.domainObject = domainObject;

        setTitle("Change " + domainName);
        setHeaderText("Change an existing " + domainName);
    }

    public void setValidator(Validator<Domain> validator) {
        this.validator = validator;
    }

    @Override
    public void setController(InputDialogController<Domain> controller) {
        controller.prepareForUpdate(domainObject);

        super.setController(controller);
    }

    @Override
    protected void validate(Domain domainObject) throws ValidationException {
        assert validator != null;

        validator.validateForUpdate(domainObject);
    }
}