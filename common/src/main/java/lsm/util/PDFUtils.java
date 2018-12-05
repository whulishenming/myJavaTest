package lsm.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author lishenming
 * @date 2018/11/7 13:18
 **/
public class PDFUtils {

    public static void fillTemplate() {
        String templatePath = "/Users/lishenming/Downloads/超级班车迟到证明-模板.pdf";

        // 生成的新文件路径
        String newPDFPath = "/Users/lishenming/Downloads/超级班车迟到证明-18621949186.pdf";
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            // 输出流
            out = new FileOutputStream(newPDFPath);
            // 读取pdf模板
            reader = new PdfReader(templatePath);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();

            String[] str = { "7317828652", "18621949186", "12月31日", "临空江桥一号线", "07点50分", "", "55", "" };
            int i = 0;

            form.setField("orderId","7317828652");
            form.setField("mobile","18621949186");
            form.setField("fromTime","2018年12月31日 07点50分");
            form.setField("lineAlias","临空江桥一号线");
            form.setField("fromArea","九新公路沪松公路公交站台处");
            form.setField("toArea","SOHO-16号楼与18号楼中间，taxi停靠点");
            form.setField("delayTime","55");


//            java.util.Iterator<String> it = form.getFields().keySet().iterator();
//            while (it.hasNext()) {
//                String name = it.next();
//                System.out.println(name);
//                form.setField(name, str[i++]);
//            }
            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.setFormFlattening(true);
            stamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (IOException e) {
            System.out.println(1);
        } catch (DocumentException e) {
            System.out.println(2);
        }

    }

    public static void main(String[] args) {
//        fillTemplate();
        System.out.println(UUID.randomUUID().toString().toLowerCase());
    }
}
