# PDF_Cut-Stack
Program to impose a pdf file on a larger page as a 2 pages "cut and stack" printout.
 
## Prerequisites

Developed and tested with:
 + Windows 7 64 bit
 + NetBeans IDE 8.0.2
 + java version "1.8.0_25"
 + Java(TM) SE Runtime Environment (build 1.8.0_25-b18)
 + iText-5.0.1jar library [(https://sourceforge.net/projects/itext/files/iText/iText5.0.1)]  
  
## Getting Started
Download Signature_pdf.jar, PDF_cutnstack.cfg and Courier-Normal Regular.ttf in your working directory.  
Change/modify parameter in PDF_cutnstack.cfg to your requirements and run with:  

  >java -jar Signature_pdf.jar \<source\> \<dest> \<sides> \<markers> \<font> \<size> \<outwidth> \<outlength> \<inwidth> \<inlength> \<offset>  

| **Parameter**  |  **Meaning** |
| ---------- | -------- |
|  source    |  input pdf filename  |
|  dest      |  output pdf filename  |
|  sides     |  double side or single side output:  |
|            |  d - double side, short/left duplex pairing  |
|            |  s - single side simplex  |
|  markers   |  dimension (dpi) of cut markers  |
|  font      |  font filename to be used when numbering pages  |
|  size      |  font dimension (dpi) used when numbering pages if set to 0 no number printing  |
|  outwidth  |  desired ouput pdf file width dimension (cm)  |
|  outlength |  desired ouput pdf file length dimension (cm)  |
|  inwidth   |  desired input pdf file width dimension (cm)  |
|  inlength  |  desired input pdf file length dimension (cm)  |
|  offset    |  offset of imposed pages:  |
|            |  0 - left and right centered on page  |
|            |  1 - left and right center paired  |
|  seppages  |  separator pages:  |
|            |  0 - ignore  |
|            |  x - add x   |
|            |  0 - left and right centered on page  |
|            |  1 - left and right center paired  |
|  seppages  | separator pages:  |
|            |  0 - ignore  |
|            |  x - add x   |

<p>
Example: a 211 A4 pages pdf will be imposed as a 108 pages A3 which can be cut in half and stacked left pile over right pile. NOTE As 211 is not a multiple of 4 (pages) the output printout will
contain extra blank pages to fulfill this requirement.
<p>
If you want to compile/modify simply download PDF_CutnStack.java source, create a NetBeans or Eclipse IDE project, add *itext-5.0.1.jar* library and add/modify and compile  
