#!/usr/bin/perl
###############################################################################
# Reciprocal Net project - portal software
# molsubmiter.pl
#
# Simple PERL script that accepts an HTTP form submission and e-mails the 
# form's contents somewhere.  Intended to be used in conjunction with
# commonmoleculetext.html and commonmoleculetext-thankyou.html to accept 
# submissions of text by (unauthenticated) contributors that will be attached
# to the sample metadata records of "common molecules".
#
# 08-Sep-2002: rlauer wrote first draft
# 11-Sep-2002: ekoperda readied script for deployment on the portal site
# 11-Oct-2004: jchong changed field names to match a revised web form
##############################################################################


# path of mail program for sending form data
# currently sendmail is supported
$Mailer = '/usr/sbin/sendmail';

# destination email address of form submission data.
$email_listserv = 'cmtsub-l@reciprocalnet.org';

#path to Thank you page after form submission
$ThankYouPage = "/edumodules/commonmolecules/requestconfirm.html";

### MAIN PROG #########
#TODO: currently no form validation implemented
&FormParse;

#name of submitter
$name = $FormData{'name'};

#email address of submitter
$email_subs = $FormData{'email'};

#compound (ie. chemical name) of submission
$compound = $FormData{'compound'};

#short sentence description of compound
$formula = $FormData{'formula'};

#Explanation on a certain aspect of compound
$description = $FormData{'description'};

#All sources used in formulating submission
$suggestion = $FormData{'suggestion'};

&SendMail;
#redirect to a submission confirmation page
print "Location: $ThankYouPage\n\n";

exit;

#######################
# SendMail
# Opens sendmail and pipes mail message out
# on close;
sub SendMail {
  open (SENDMAIL, "|$Mailer -t");
  print SENDMAIL "To: $email_listserv\n";
  print SENDMAIL "Subject: Request from Reciprocal Net User\n";
  print SENDMAIL "Message: \n";
  print SENDMAIL "Submitted by: $name\n";
  print SENDMAIL "Email: $email_subs\n";
  print SENDMAIL "--" x 11 . "\n";    #divider
  print SENDMAIL "Compound:\n";
  print SENDMAIL "$compound\n";
  print SENDMAIL "--" x 11 . "\n";    #divider
  print SENDMAIL "Formula:\n";
  print SENDMAIL "$formula\n";
  print SENDMAIL "--" x 11 . "\n";    #divider
  print SENDMAIL "Description:\n";
  print SENDMAIL "$description\n";
  print SENDMAIL "--" x 11 . "\n";    #divider
  print SENDMAIL "Suggestion:\n";
  print SENDMAIL "$suggestion\n";
  print SENDMAIL "-" x 11 . "\n";    #divider
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
