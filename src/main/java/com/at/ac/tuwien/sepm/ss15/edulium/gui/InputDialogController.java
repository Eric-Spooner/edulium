package com.at.ac.tuwien.sepm.ss15.edulium.gui;

public interface InputDialogController<Domain> {
    void prepareForCreate();

    void prepareForUpdate(Domain object);

    void prepareForSearch();

    Domain toDomainObject();
}
