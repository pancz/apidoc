package com.zhaoyun.docmanager.core.lucene;

import com.zhaoyun.docmanager.common.commons.spring.config.SpringConfig;
import com.zhaoyun.docmanager.core.exception.BizException;
import com.zhaoyun.docmanager.core.util.HttpClientUtil;
import com.zhaoyun.docmanager.core.util.StringUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/8/10 13:19.
 * 未来不确定才更值得前行
 */
public class IndexSearch {
    protected static Logger LOGGER = LoggerFactory.getLogger(IndexSearch.class);
    private static Analyzer analyzer = null;
    private static String INDEX_DIR;
    private static Directory directory = null;
    private static IndexWriter indexWriter = null;

    static {
        INDEX_DIR = SpringConfig.get("LUCENE_ROOT_PATH");
        if (StringUtil.isEmpty(INDEX_DIR)) {
            INDEX_DIR = "/zhaoyun/lucene";
        }
    }

    public static void main(String[] args) throws Exception {
   /*     SearchIndexBean b = new SearchIndexBean();
        b.setApiDesc("限时抢购活动页数�?");
        b.setApiName("flashsale");
        b.setKey("1_2");
        b.setKeyId(1);
        b.setKeyType(2);
        b.setServiceDesc("红包接口");
        b.setServiceName("com zhaoyun bmarketprod facade service html HtmlFacade");
        List<SearchIndexBean> bList = new ArrayList<>();
        bList.add(b);
        addIndex(bList);*/
//        deleteIndex(1, 2);
//        System.out.println(searchIndex("根据店铺", 2));
        String s = HttpClientUtil.get("http://192.168.2.126:8081/nexus/content/groups/public/com/zhaoyun/cyb/common-maven/maven-metadata.xml");
        System.out.println(s);

    }

    public static void addIndex(List<SearchIndexBean> beanList) {
        try {
            analyzer = new StandardAnalyzer(Version.LUCENE_40);
            directory = FSDirectory.open(new File(INDEX_DIR));
            IndexWriterConfig config = new IndexWriterConfig(
                    Version.LUCENE_40, analyzer);
            if (IndexWriter.isLocked(directory)) {
                indexWriter.close();
            }
            indexWriter = new IndexWriter(directory, config);
            for (SearchIndexBean bean : beanList) {
                Document doc = new Document();
                doc.add(new TextField(SearchIndexBean.API_DESC, bean.getApiDesc() == null ? "" : bean.getApiDesc(), Field.Store.YES));
                doc.add(new TextField(SearchIndexBean.API_NAME, bean.getApiName() == null ? "" : bean.getApiName(), Field.Store.YES));
                doc.add(new TextField(SearchIndexBean.KEY, bean.getKeyId() + "_" + bean.getKeyType(), Field.Store.YES));
                doc.add(new TextField(SearchIndexBean.SERVICE_DESC, bean.getServiceDesc() == null ? "" : bean.getServiceDesc(), Field.Store.YES));
                doc.add(new TextField(SearchIndexBean.SERVICE_NAME, bean.getServiceName().replace(".", " "), Field.Store.YES));
                doc.add(new IntField(SearchIndexBean.KEY_ID, bean.getKeyId(), Field.Store.YES));
                doc.add(new IntField(SearchIndexBean.KEY_TYPE, bean.getKeyType(), Field.Store.YES));
                doc.add(new IntField(SearchIndexBean.POM_ID, bean.getPomId(), Field.Store.YES));
                indexWriter.addDocument(doc);
            }
            indexWriter.commit();
            if (indexWriter.isLocked(directory)) {
                indexWriter.close();
            }
        } catch (Exception e) {
            LOGGER.error("增加索引失败", e);
            throw new BizException(e);
        }

    }

    public static void deleteIndex(int keyId, int keyType) throws IOException {
        analyzer = new StandardAnalyzer(Version.LUCENE_40);
        directory = FSDirectory.open(new File(INDEX_DIR));
        IndexWriterConfig config = new IndexWriterConfig(
                Version.LUCENE_40, analyzer);
        indexWriter = new IndexWriter(directory, config);
        indexWriter.deleteDocuments(new Term(SearchIndexBean.KEY, keyId + "_" + keyType));
        indexWriter.close();
    }

    public static void deleteIndexByPomId(int pomId) throws IOException {
        analyzer = new StandardAnalyzer(Version.LUCENE_40);
        directory = FSDirectory.open(new File(INDEX_DIR));
        IndexWriterConfig config = new IndexWriterConfig(
                Version.LUCENE_40, analyzer);
        indexWriter = new IndexWriter(directory, config);
        BytesRef bytes = new BytesRef(NumericUtils.BUF_SIZE_INT);
        NumericUtils.intToPrefixCoded(pomId, 0, bytes);
        Term term = new Term(SearchIndexBean.POM_ID, bytes);
        indexWriter.deleteDocuments(term);
        indexWriter.close();
    }

    public static List<SearchIndexBean> searchIndex(String str, int pageSize) throws IOException, ParseException {
        directory = FSDirectory.open(new File(INDEX_DIR));
        analyzer = new StandardAnalyzer(Version.LUCENE_40);
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(ireader);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_40,
                new String[]{SearchIndexBean.API_DESC, SearchIndexBean.API_NAME, SearchIndexBean.SERVICE_DESC, SearchIndexBean.SERVICE_NAME},
                analyzer);
        Query query = parser.parse(str);
        ScoreDoc[] hits = searcher.search(query, null, pageSize).scoreDocs;
        List<SearchIndexBean> l = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            Document hitDoc = searcher.doc(hit.doc);
            l.add(SearchIndexBean.toBeanFromIndex(hitDoc));
        }
        ireader.close();
        directory.close();
        return l;
    }


}
