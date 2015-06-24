package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import com.at.ac.tuwien.sepm.ss15.edulium.domain.validation.ValidationException;

public class SearchInputDialog<Domain> extends InputDialog<Domain> {

    public SearchInputDialog(String domainName) {
        super(domainName);

        setTitle("Search for " + domainName);
        setHeaderText("Search for existing " + domainName);
    }

    @Override
    public void setController(InputDialogController<Domain> controller) {
        controller.prepareForSearch();

        super.setController(controller);
    }

    @Override
    protected void validate(Domain domainObject) throws ValidationException {

    }
}