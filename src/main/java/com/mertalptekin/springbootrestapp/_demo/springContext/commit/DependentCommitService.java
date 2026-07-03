package com.mertalptekin.springbootrestapp._demo.springContext.commit;

import org.springframework.stereotype.Service;

// Eğer High Level Class + Low Class birbirine direk class olarak bağımlıysa di osa bile tight coupled -> sıkı bağlı
// Arada interface olmadan çalıştığımızı düşünelim
// DependentCommitService -> High Level
// Burası kırılkanlık yaratan bir kod örneğidir.
@Service
public class DependentCommitService {

    private  final  DbCommit dbCommit; // Low Level Class


    // Bağımlıkları kırmak için Dependecy Injection yaptık ama DependentCommitService sınıfın DbCommit olan bağımlılığı hala mevcut.
    public DependentCommitService(DbCommit dbCommit){
        this.dbCommit = dbCommit;
    }

    public void  save(){
        this.dbCommit.commitChanges();
    }


}

// DependentCommitService -> DbCommit sınıfına bağlı
// Senaryo gereği DependentCommitService gibi 100 farklı servis sınıfında dbCommit yerine CacheCommit
// sınıfı ile çalışmak istendiğinde  technical debt -> 100 referans güncellenmeli. refactoring.