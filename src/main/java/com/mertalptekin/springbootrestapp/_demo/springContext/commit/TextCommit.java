package com.mertalptekin.springbootrestapp._demo.springContext.commit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("textCommit")
public class TextCommit implements ICommit {
    @Override
    public void commitChanges() {
        System.out.println("Text Commit");
    }
}
