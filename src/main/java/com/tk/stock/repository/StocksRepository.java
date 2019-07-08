package com.tk.stock.repository;

        import com.tk.stock.model.Stock;
        import org.springframework.data.elasticsearch.annotations.Document;
        import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
        import org.springframework.data.elasticsearch.repository.support.AbstractElasticsearchRepository;
        import org.springframework.stereotype.Component;

@Component
public interface StocksRepository extends ElasticsearchRepository<Stock,Long> {
}
