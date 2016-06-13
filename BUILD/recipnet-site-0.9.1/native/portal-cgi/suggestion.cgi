#!/usr/bin/perl
###############################################################################
# Reciprocal Net project - portal software
# suggestion.pl
#
# Simple PERL script that accepts an HTTP form submission and e-mails the 
# form's contents somewhere.  Intended as a means to accept feedback from users.
#
# 21-Nov-2002: lsandvos made script borrowing heavily from commonmoletext.cgi
##############################################################################


# path of mail program for sending form data
# currently sendmail is supported
$Mailer = '/usr/sbin/sendmail';

#path to Thank you page after form submission
$ThankYouPage = "/contributions/suggestion-thankyou.html";

# destination email address of form submission data.
$email_listserv = 'info@reciprocalnet.org';

### MAIN PROG #########
#TODO: currently no form validation implemented
&FormParse;

#email address of submitter
$email_subs = $FormData{'email'};

#name of submitter
$name = $FormData{'name'};

#short sentence description of compound
$URL = $FormData{'URL'};

#Explanation on a certain aspect of compound
$comment = $FormData{'comment'};

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
  print SENDMAIL "Subject: RecipNet Comment or Suggestion\n";
  print SENDMAIL "Submitted by: $name\n";
  print SENDMAIL "Email: $email_subs\n";
  print SENDMAIL "-" x 22 . "\n";    #divider
  print SENDMAIL "URL of page commenting on:\n";
  print SENDMAIL "$URL\n";
  print SENDMAIL "-" x 22 . "\n";    #divider
  print SENDMAIL "Comment or suggestion:\n";
  print SENDMAIL "$comment\n";
  print SENDMAIL "-" x 22 . "\n";    #divider
  print SENDMAIL "End of Comments and Suggestions\n";
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
