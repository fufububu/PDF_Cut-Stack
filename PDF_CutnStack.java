package PDF_CutnStack;

import java.io.*;
import java.util.Properties;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.Font;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;

/**
 * Program to impose a pdf file on a larger page as a 2 pages "cut and stack" printout.
 * <p>
 * Example: a 211 A4 pages pdf will be imposed as a 108 pages A3 which can be cut in half and stacked left pile over right pile. NOTE As 211 is not a multiple of 4 (pages) the output printout will
 * contain extra blank pages to fulfill this requirement.
 * <p>
 * Parameters are passed with command line arguments:
 * <ul>
 * <li> args[0] = source pdf filename
 * <li> args[1] = destination pdf filename
 * <li> args[2] = destination double sided (f/r paired on short page margin- left) or only front (f)
 * <li> args[3] = length (dpi)of cut markers
 * <li> args[4] = ttf font to be used when numbering pages is set
 * <li> args[5])= font size (dpi) when numbering pages is set, only if <> 0, if set to 0 no numbering is made
 * <li> args[6])= output paper width size (cm)
 * <li> args[7])= output paper length size (cm)
 * <li> args[8])= input paper width size (cm)
 * <li> args[9])= input paper length size (cm)
 * <li> args[10])= offset of pages : 0 = centered on impoed page, 1 = left and right centered paired  *
 * <li> args[11])= separator pages : 0 = ignore, x = add x pages  *
 * </ul>
 *
 * @throws com.itextpdf.text.DocumentException
 * @throws java.io.IOException
 * @version 1.0
 * @author fulvio
 */
public class PDF_CutnStack {

    public static void main(String[] args) throws DocumentException, IOException {

        String s_src = "";
        String s_dest = "";
        String s_fr = "";
        String s_tempfile = "";
        String s_font = "";
        int i_marklenght = 0;
        int i_offset = 0;
        int i_fontsize = 0;
        int i_seppages = 0;
        double d_output_paperwidth = 0;
        double d_output_paperlenght = 0;
        double d_input_paperwidth = 0;
        double d_input_paperlenght = 0;
        double remainder = 0;
        boolean DUPLEX = true;

        String current_dir = new java.io.File(".").getCanonicalPath() + "\\";

        File config_file = new File(current_dir + "PDF_cutnstack.cfg");

        if ((!config_file.exists()) && (args.length != 12)) {
            System.out.println("Missing PDF_cutnstack.cfg conf file or wrong number of parameters !!");
            System.out.println("**************************************************");
            System.out.println("* Program to impose a pdf file on a larger       *");
            System.out.println("* page as a 2up pages \"cut and stack\" printout.*");
            System.out.println("**************************************************");
            System.out.println("Proper Usage is:");
            System.out.println("       java -jar Signature_pdf.jar <source> <dest> <sides> <markers> <font> <size> <outwidth> <outlength> <inwidth> <inlength> <offset>");
            System.out.println("<source>   : input pdf filename");
            System.out.println("<dest>     : output pdf filename");
            System.out.println("<sides>    : double side or single side output:");
            System.out.println("                                              d - double side, short/left duplex pairing");
            System.out.println("                                              s - single side simplex");
            System.out.println("<markers>  : dimension (dpi) of cut markers");
            System.out.println("<font>     : font filename to be used when numbering pages");
            System.out.println("<size>     : font dimension (dpi) used when numbering pages if set to 0 no number printing");
            System.out.println("<outwidth> : desired ouput pdf file width dimension (cm)");
            System.out.println("<outlength>: desired ouput pdf file length dimension (cm)");
            System.out.println("<inwidth>  : desired input pdf file width dimension (cm)");
            System.out.println("<inlength> : desired input pdf file length dimension (cm)");
            System.out.println("<offset>   : offset of imposed pages:");
            System.out.println("                                    0 - left and right centered on page");
            System.out.println("                                    1 - left and right center paired");
            System.out.println("<seppages> : separator pages:");
            System.out.println("                            0 - ignore");
            System.out.println("                            x - add x ");
            System.exit(0);

            System.out.println("Usage:");
            System.exit(0);

        } else if (config_file.exists()) {
            try (FileInputStream in = new FileInputStream(config_file)) {
                Properties prop = new Properties();
                prop.load(in);
                in.close();

                s_src = prop.getProperty("source");                                             // source pdf filename                                                                           
                s_dest = prop.getProperty("destination");                                       // destination pdf filename                                                                      
                s_fr = prop.getProperty("doublesided");                                         // destination double sided (f/r paired on short page margin- left) or only front (f)            
                i_marklenght = Integer.valueOf(prop.getProperty("marklenght"));                 // length (dpi)of cut markers                                                                    
                s_font = prop.getProperty("font");                                              // ttf font to be used when numbering pages is set                                               
                i_fontsize = Integer.valueOf(prop.getProperty("fontsize"));                     // font size (dpi) when numbering pages is set, only if <> 0, if set to 0 no numbering is made   
                d_output_paperwidth = Double.valueOf(prop.getProperty("output_paperwidth"));    // output paper width size (cm)                                                                  
                d_output_paperlenght = Double.valueOf(prop.getProperty("output_paperlenght"));  // output paper length size (cm)                                                                 
                d_input_paperwidth = Double.valueOf(prop.getProperty("d_input_paperwidth"));    // input paper width size (cm)                                                                   
                d_input_paperlenght = Double.valueOf(prop.getProperty("d_input_paperlenght"));  // input paper length size (cm)                                                                  
                i_offset = Integer.valueOf(prop.getProperty("offset"));                         // offset of pages : 0=centered on impoed page, 1=left and right centered paired 
                i_seppages = Integer.valueOf(prop.getProperty("seppages"));                     // separator pages : 0= ignore, x = add x pages

                s_tempfile = current_dir + "temp.pdf";

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {

            s_src = current_dir + args[0];                   // source pdf filename                                                                             
            s_dest = current_dir + args[1];                  // destination pdf filename                                                                        
            s_fr = args[2];                                  // destination double sided (f/r paired on short page margin- left) or only front (f)              
            i_marklenght = Integer.valueOf(args[3]);         // length (dpi)of cut markers                                                                      
            s_font = current_dir + args[4];                  // ttf font to be used when numbering pages is set                                                 
            i_fontsize = Integer.valueOf(args[5]);           // font size (dpi) when numbering pages is set, only if <> 0, if set to 0 no numbering is made     
            d_output_paperwidth = Double.valueOf(args[6]);   // output paper width size (cm)                                                                    
            d_output_paperlenght = Double.valueOf(args[7]);  // output paper length size (cm)                                                                   
            d_input_paperwidth = Double.valueOf(args[8]);    // input paper width size (cm)                                                                     
            d_input_paperlenght = Double.valueOf(args[9]);   // input paper length size (cm)                                                                    
            i_offset = Integer.valueOf(args[10]);            // offset of pages : 0=centered on impoed page, 1=left and right centered paired 
            i_seppages = Integer.valueOf(args[11]);          // separator pages : 0= ignore, x = add x pages

            s_tempfile = current_dir + "temp.pdf";
        }
        //TODO check for locked files
        float missing_pages = 0;
        int i = 1;
        int x1_posleft;
        int y1_posleft;
        int x2_posright;
        int y2_posright;
        int x1_fl;//x front left 
        int y1_fl;//y front left 
        int x2_fr;//x front right 
        int y2_fr;//y front right 
        int x1_rl;//x rear left 
        int y1_rl;//y rear left 
        int x2_rr;//x rear right 
        int y2_rr;//y rear right 
        int temp;

        double d_dpi = 72; // dot per inch=dpi
        double d_inch = 2.54;

        // input/output pagesizes default dimensions dpi
        double d_pagewidth_dot = (d_output_paperwidth / d_inch) * d_dpi;
        double d_pageheight_dot = (d_output_paperlenght / d_inch) * d_dpi;
        double d_inputwidth_dot = (d_input_paperwidth / d_inch) * d_dpi;
        double d_inputheight_dot = (d_input_paperlenght / d_inch) * d_dpi;

        DUPLEX = "d".equals(s_fr);

        /* Get input file number of pages and evaluate missing pages
         * to complete 2UP imposition. 
         * Each 2UP output page should contain 4 original input file pages
         * Input number of pages must be exact mutiple of 4 */
        PdfReader reader = new PdfReader(s_src);
        int numpages = reader.getNumberOfPages();
        int half_numpages = numpages / 2;

        if (DUPLEX) {
            remainder = numpages % 4;
            missing_pages = (float) (4.0 - remainder);
        } else {
            remainder = numpages % 2;
            missing_pages = (float) (2.0 - remainder);
        }

        // Create temporary file with missing pages addded
        File file = new File(s_tempfile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(s_tempfile));
        if (missing_pages != 0) { //se mancano dele pagine allora aggiungile
            while (i <= missing_pages) {
                stamper.insertPage(numpages + 1, reader.getPageSizeWithRotation(1));
                i++;
            }
        }
        stamper.close();
        reader.close();

        // Source is now temporary file with exact (4 pages multiple) num pages
        s_src = s_tempfile;

        // Create output document with desiread size
        Rectangle mediabox = new Rectangle((int) d_pagewidth_dot, (int) d_pageheight_dot);
        Document document = new Document(mediabox);

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(s_dest));

        // Open output document
        document.open();

        PdfContentByte content = writer.getDirectContent();

        reader = new PdfReader(s_src);
        numpages = reader.getNumberOfPages();
        if (DUPLEX) {
            half_numpages = ((numpages / 4) * 2)-1;
        } else {
            half_numpages = numpages / 2;
        }

        PdfImportedPage page;
        BaseFont bf_times = BaseFont.createFont("C:/java develop/addextramargin/AddExtraMargin/cour.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
        Font times = new Font(bf_times, i_fontsize);
        String frase;
        Phrase phrase;
        i = 1;

        if (i_offset == 0) {
            i_offset = 0;
        } else {
            i_offset = (int) (((d_pagewidth_dot / 2) - d_inputwidth_dot)) / 2;
        }
        //
        //       -------------------------------
        //       |                             | 
        //       |    ---------   ---------    |
        //       |    |       |   |       |    |
        //       |    |       |   |       |    |
        //       |    |       |   |       |    |
        //       |    |       |   |       |    |
        //       |    |       |   |       |    |
        //       |    |       |   |       |    |
        //    y1 |.....--------   ---------....|y2
        //       |    .           .            | 
        //       |    .           .            | 
        //       -------------------------------
        //            x1          x2 
        //
        x1_posleft = (int) ((d_pagewidth_dot / 2) - d_inputwidth_dot) - i_offset;
        y1_posleft = (int) ((d_pageheight_dot - d_inputheight_dot) / 2);
        x2_posright = (int) ((d_pagewidth_dot / 2) - d_inputwidth_dot) + (int) d_inputwidth_dot + i_offset;
        y2_posright = (int) ((d_pageheight_dot - d_inputheight_dot) / 2);

        // front pages left & right coordinates
        x1_fl = x1_posleft;
        y1_fl = y1_posleft;
        x2_fr = x2_posright;
        y2_fr = y2_posright;
 
        while (i <= half_numpages) {

            // FRONT cut marks left&right
            // Get left page
            Add_cut_marks(x1_fl, y1_fl, i_marklenght, content, d_inputwidth_dot, d_inputheight_dot);
            page = writer.getImportedPage(reader, i);
            content.addTemplate(page, x1_fl, y1_fl);

            // Add left pagenumber
            if (i_fontsize != 0) {
                frase = Integer.toString(i);
                phrase = new Phrase(frase, times);
                ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phrase, (int) (x1_fl + d_inputwidth_dot - i_fontsize), y1_fl, 90);
            }

            // Get right page
            Add_cut_marks(x2_fr, y2_fr, i_marklenght, content, d_inputwidth_dot, d_inputheight_dot);
            page = writer.getImportedPage(reader, i + half_numpages);
            content.addTemplate(page, x2_fr, y2_fr);
            
            // Add right pagenumber
            if (i_fontsize != 0) {
                frase = Integer.toString(i + half_numpages);
                phrase = new Phrase(frase, times);
                ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phrase, (int) (x2_fr + d_inputwidth_dot - i_fontsize), y2_posright, 90);
            }

            document.newPage();
            document.setPageSize(mediabox);

            i++;
            if (DUPLEX) {
                // If double sided swap rear pages insertion coordinates to obtain short side/left border duplex page pairing
                // rear pages left & right coordinates
                x1_rl = x1_posleft;
                y1_rl = y1_posleft;
                x2_rr = x2_posright;
                y2_rr = y2_posright;
                
                // REAR cut marks left&right
                // Add pagenumbers
                Add_cut_marks(x2_rr, y2_rr, i_marklenght, content, d_inputwidth_dot, d_inputheight_dot);
                page = writer.getImportedPage(reader, i);
                content.addTemplate(page, x2_rr, y2_rr);

                if (i_fontsize != 0) {
                    frase = Integer.toString(i);
                    phrase = new Phrase(frase, times);
                    ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phrase, (int) (x2_rr + d_inputwidth_dot - i_fontsize), y2_rr, 90);
                }

                Add_cut_marks(x1_rl, y1_rl, i_marklenght, content, d_inputwidth_dot, d_inputheight_dot);
                page = writer.getImportedPage(reader, i + half_numpages);
                content.addTemplate(page, x1_rl, y1_rl);

                if (i_fontsize != 0) {
                    frase = Integer.toString(i + half_numpages);
                    phrase = new Phrase(frase, times);
                    ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phrase, (int) (x1_rl + d_inputwidth_dot - i_fontsize), y1_rl, 90);
                }

                document.newPage();
                document.setPageSize(mediabox);
                i++;
            }
        }
        i = 1;
        if (i_seppages != 0) {
            //Add separator pages
            while (i <= i_seppages) {
                document.newPage();
                document.setPageSize(mediabox);
                Add_separator(x1_posleft, y1_posleft, content, d_inputwidth_dot, d_inputheight_dot);
                Add_separator(x2_posright, y2_posright, content, d_inputwidth_dot, d_inputheight_dot);
                // if DUPLEX add rear
                if (DUPLEX) {
                    document.newPage();
                    document.setPageSize(mediabox);
                    Add_separator(x1_posleft, y1_posleft, content, d_inputwidth_dot, d_inputheight_dot);
                    Add_separator(x2_posright, y2_posright, content, d_inputwidth_dot, d_inputheight_dot);
                }
                i++;
            }
        }
        document.close();
        reader.close();
    }

    /**
     *
     * <li> @param xpos
     *
     * @param ypos
     * @param lenght_cut_mark
     * @param content
     * @param d_inputwidth_dot
     * @param d_inputheight_dot
     */
    public static void Add_cut_marks(int xpos, int ypos, int lenght_cut_mark, PdfContentByte content, double d_inputwidth_dot, double d_inputheight_dot) {

        content.setCMYKColorStrokeF(0f, 0f, 0f, 1f);

        content.moveTo(xpos, ypos - lenght_cut_mark);
        content.lineTo(xpos, ypos);

        content.moveTo(xpos + (int) d_inputwidth_dot, ypos - lenght_cut_mark);
        content.lineTo(xpos + (int) d_inputwidth_dot, ypos);

        content.moveTo(xpos, ypos + (int) d_inputheight_dot);
        content.lineTo(xpos, ypos + (int) d_inputheight_dot + lenght_cut_mark);

        content.moveTo(xpos + (int) d_inputwidth_dot, ypos + (int) d_inputheight_dot);
        content.lineTo(xpos + (int) d_inputwidth_dot, ypos + (int) d_inputheight_dot + lenght_cut_mark);

        content.closePathStroke();

    }

    /**
     *
     * @param xpos
     * @param ypos
     * @param content
     * @param d_inputwidth_dot
     * @param d_inputheight_dot
     */
    public static void Add_separator(int xpos, int ypos, PdfContentByte content, double d_inputwidth_dot, double d_inputheight_dot) {

        content.setCMYKColorStrokeF(0f, 0f, 0f, 1f);
        content.setColorFill(BaseColor.LIGHT_GRAY);
        content.rectangle(xpos, ypos, (int) d_inputwidth_dot, (int) d_inputheight_dot);
        content.fillStroke();

        content.moveTo(xpos, ypos);
        content.lineTo(xpos + (int) d_inputwidth_dot, (int) (ypos + d_inputheight_dot));
        content.moveTo(xpos + (int) d_inputwidth_dot, ypos);
        content.lineTo(xpos, (int) (ypos + d_inputheight_dot));

        content.closePathStroke();

    }
}
