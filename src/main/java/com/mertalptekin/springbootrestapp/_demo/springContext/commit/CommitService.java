package com.mertalptekin.springbootrestapp._demo.springContext.commit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

// CommitService High Level Class  ICommit ten türeyen TextCommit, DbCommit vs gibi Low Level classlara direk bağlı değil
// interface üzerinden bağlıdır. loose coupled (zayıf bağlılık)
// OCP ile DIP uygundur. ve DI destekli
@Service
public class CommitService {

    // DIP CommitService hignLevel Class ICommit intterfaceden türüyen herhangi bir CacheCommit veya DbCommit lowLevel classlarını bilmemeli.
    // Depency Inversion Principle (DIP) uygulanmalı.
    private final ICommit commit;
    // araya interface koyduğundan dolayı runtime da commit servis kararı verilebilir yada
    // bir altyapıdan başka bir altyapıya geçmek çok kolay refactor maliyeti düşük.

    // DI -> @Qualifier("dbCommit") ICommit commit
    public CommitService(ICommit commit) {
        this.commit = commit;
    }

    public void save() {
        commit.commitChanges();
    }

}


// Commit Service -> DbCommit veya CacheCommit