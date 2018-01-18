# PDF_Cut-Stack
Program to impose a pdf file on a larger page as a 2 pages "cut and stack" printout.
<p>
Example: a 211 A4 pages pdf will be imposed as a 108 pages A3 which can be cut in half and stacked left pile over right pile. NOTE As 211 is not a multiple of 4 (pages) the output printout will
contain extra blank pages to fulfill this requirement.
<p>
Parameters are passed with command line arguments:
<ul>
<li> args[0] = source pdf filename
<li> args[1] = destination pdf filename
<li> args[2] = destination double sided (f/r paired on short page margin- left) or only front (f)
<li> args[3] = length (dpi)of cut markers
<li> args[4] = ttf font to be used when numbering pages is set
<li> args[5])= font size (dpi) when numbering pages is set, only if <> 0, if set to 0 no numbering is made
<li> args[6])= output paper width size (cm)
<li> args[7])= output paper length size (cm)
<li> args[8])= input paper width size (cm)
<li> args[9])= input paper length size (cm)
<li> args[10])= offset of pages : 0 = centered on impoed page, 1 = left and right centered paired  *
<li> args[11])= separator pages : 0 = ignore, x = add x pages  *
</ul>  

Proper Usage is:  
<ul> java -jar Signature_pdf.jar \<source\> <dest> <sides> <markers> <font> <size> <outwidth> <outlength> <inwidth> <inlength> <offset>");  
<li> <source>   : input pdf filename");
<li> <dest>     : output pdf filename");
<li> <sides>    : double side or single side output:");
<li>                                               d - double side, short/left duplex pairing");
<li>                                               s - single side simplex");
<li> <markers>  : dimension (dpi) of cut markers");
<li> <font>     : font filename to be used when numbering pages");
<li> <size>     : font dimension (dpi) used when numbering pages if set to 0 no number printing");
<li> <outwidth> : desired ouput pdf file width dimension (cm)");
<li> <outlength>: desired ouput pdf file length dimension (cm)");
<li> <inwidth>  : desired input pdf file width dimension (cm)");
<li> <inlength> : desired input pdf file length dimension (cm)");
</ul><offset>   : offset of imposed pages:");
<li>                                     0 - left and right centered on page");
<li>                                     1 - left and right center paired");
<li> <seppages> : separator pages:");
<li>                             0 - ignore");
<li>                            x - add x ");
</ul>
