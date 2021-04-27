package org.meier.model;

import org.meier.bean.NameTypeBean;

import java.util.List;

public interface CodeContainer {

    List<NameTypeBean> getVariables();
    void addVariable(NameTypeBean variable);

}
