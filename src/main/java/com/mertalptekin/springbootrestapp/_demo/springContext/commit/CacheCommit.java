package com.mertalptekin.springbootrestapp._demo.springContext.commit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component // Bean ismi cacheCommit olarak tanımlandı
@Primary // Bu sınıf ICommit arayüzünün birincil uygulaması olarak işaretlendi, ICommit olarak CacheCommit sınıfını kullan.
@Qualifier("cacheCommit")
public class CacheCommit implements ICommit {
    @Override
    public void commitChanges() {
        System.out.println("Cache Commit changes");
    }
}
