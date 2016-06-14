#!/usr/bin/perl 
###############################################################################
# Reciprocal Net project 
# exceptionreport.cgi
#
# Simple PERL script that accepts an HTTP form submission from a user of some
# Reciprocal Net partner site, from the site software's error.jsp page.  A
# convenient way for users to report details of their exceptions to tech
# support.  Current implementation e-mails the results of the form post.
#
# 10-Mar-2003: midurbin wrote first draft 
###############################################################################


# path of mail program for sending form data
# currently sendmail is supported
$Mailer = '/usr/sbin/sendmail';

# destination email address of form submission data.
$target_email = 'help@reciprocalnet.org';

### MAIN PROG #########
&FormParse;

# ip address of user who submitted error report
$ip = $FormData{'ip'};

# URL of page from which exception was thrown
$url = $FormData{'url'};

# parameters from http requst
$params = $FormData{'params'};

# name of the exception thrown
$exception = $FormData{'except'};

# message included within exception object (may not be present)
$message = $FormData{'msg'};

# comments entered by submitter
$comments = $FormData{'cmnts'};

# stack trace
$stackTrace = $FormData{'trace'};

&SendMail;

#display simple html message
print "Content-type: text/html\n\n";
print "<head><title>Error Report Submitted</title></head>";
print "<body bgcolor=\"#ffffff\">";
print "Your exception report has been submitted ";
print "to Reciprocal Net technical support personnel.  If you require ";
print "additional assistance you should contact manually the administrator ";
print "of the site where the error occurred, or Reciprocal Net technical ";
print "support.";
print "</body>";
exit;

#######################
# SendMail
# Opens sendmail and pipes mail message out
# on close;
sub SendMail {
  open (SENDMAIL, "|$Mailer -t");
  print SENDMAIL "To: $target_email\n";
  print SENDMAIL "Subject: Unhandled Exception: $exception\n";
  print SENDMAIL "Submitted by: $ip\n";
  print SENDMAIL "-" x 22 . "\n";    #divider
  print SENDMAIL "User comments:\n";
  print SENDMAIL "$comments\n";
  print SENDMAIL "-" x 22 . "\n";    #divider
  print SENDMAIL "URL of page that threw exception:\n";
  print SENDMAIL "$url\n";
  print SENDMAIL "-" x 22 . "\n";    #divider
  print SENDMAIL "Parameters in HTTP request:\n";
  print SENDMAIL "$params\n";
  print SENDMAIL "-" x 22 . "\n";    #divider
  print SENDMAIL "Exception name:\n";
  print SENDMAIL "$exception\n";
  print SENDMAIL "-" x 22 . "\n";    #divider
  print SENDMAIL "Exception message:\n";
  print SENDMAIL "$message\n";
  print SENDMAIL "-" x 22 . "\n";    #divider
  print SENDMAIL "Stack trace:\n";
  print SENDMAIL "$stackTrace\n";
  print SENDMAIL "-" x 22 . "\n";    #divider
  close(SENDMAIL);
}

######################
# FormParse
# form must use POST method
sub FormParse {
  read(STDIN, $form, $ENV{'CONTENT_LENGTH'});

  @form = split(/&/,$form);
  foreach $pair (@form) {
    ($name, $value)=split(/=/, $pair);

#replace +'s with ''
	$name =~ tr/+/ /;
	$value =~ tr/+/ /;

#translate any hex values into characters again
	$name=~s/%(..)/pack("C",hex($1))/eg;
	$value=~s/%(..)/pack("C",hex($1))/eg;
#load name-value pairs into an array
        $FormData{$name}=$value;
  }
}
exit;
#### END OF PROG #########
