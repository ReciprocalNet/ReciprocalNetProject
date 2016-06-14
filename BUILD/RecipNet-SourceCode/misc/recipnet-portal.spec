###############################################################################
# Reciprocal Net project
# recipnet-portal.spec
#
# 29-Apr-2004: ekoperda wrote first draft
# 20-Nov-2006: jobollin switched the Copyright tag to a License tag to appease
#              the current version of rpmbuild
# 
###############################################################################


# BASIC INFORMATION
Name: recipnet-portal
Version: @dist.portal.release@
Release: @dist.portal.buildnumber@
Source: @dist.source-snapshot-file-truncated@
Buildroot: /tmp/recipnet-portal-@dist.portal.release@-@dist.portal.buildnumber@
Summary: Web content for http://www.reciprocalnet.org/
Group: Other
Vendor: Indiana University Molecular Structure Center
License: proprietary
URL: http://www.reciprocalnet.org/
Requires: httpd >= 2, perl >= 5
%description
Web content for http://www.reciprocalnet.org/ .


# PREP SECTION
%prep
%setup -c recipnet-portal-@dist.portal.release@


# BUILD SECTION
%build
# ain't much to do...


# INSTALL SECTION
%install
# Create the install directory
if [ -d $RPM_BUILD_ROOT ]; then
    rm -rf $RPM_BUILD_ROOT
fi
mkdir $RPM_BUILD_ROOT

# Copy static web content.
mkdir -p $RPM_BUILD_ROOT/var/www/html/
cp -R content/portal/* $RPM_BUILD_ROOT/var/www/html/
find $RPM_BUILD_ROOT/var/www/html -type f -exec chmod 644 {} \;
find $RPM_BUILD_ROOT/var/www/html -type d -exec chmod 755 {} \;

# Copy CGI scripts.
mkdir -p $RPM_BUILD_ROOT/var/www/cgi-bin/
cp native/portal-cgi/*.cgi $RPM_BUILD_ROOT/var/www/cgi-bin/
chmod -R 755 $RPM_BUILD_ROOT/var/www/cgi-bin/

# One special CGI script goes into the /master directory.
mkdir -p $RPM_BUILD_ROOT/var/www/html/master/
cp native/portal-cgi/exceptionreport.cgi $RPM_BUILD_ROOT/var/www/html/master/
chmod 755 $RPM_BUILD_ROOT/var/www/html/master/exceptionreport.cgi


# CLEAN SECTION
%clean
rm -rf $RPM_BUILD_ROOT


# FILES SECTION
%files
%attr(-, root, root) /var/www/
