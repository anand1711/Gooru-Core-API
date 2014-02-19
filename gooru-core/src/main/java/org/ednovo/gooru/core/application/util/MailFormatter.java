package org.ednovo.gooru.core.application.util;

public class MailFormatter {

	public static final String MAIL_FORGOT_PASSWORD = "<div><img src=\"%s/gooru/images/mail/gooru.png\"></div><div style=\"font-size: 13px;border: 1px solid #E5E5E5;margin: 5px 5px 5px 60px;padding: 10px;font-family: arial;\"><div><img style=\"float:left \" src=\"%s/gooru/images/mail/school.png\"><p style=\"position:relative;top:20px;\">Gooru is a search engine for learning that harnesses the power of the web by organizing free, online education resources into searchable collections, accessible from any web or mobile platform.</p></div><div style=\"clear:both;padding-top:10px;margin-left: 45px;\">Hi <span style=\"text-transform:capitalize;\">%s</span>,</div><div style=\"margin-left: 75px;margin-top: 12px;\"><div style=\"margin-top:5px;\">You or someone you know requested help to reset your password. </div><div>Click on the link below to change your Gooru password: </div><div style=\"margin-top:5px;\"><a href=\"%s\" target=\"_blank\" title=\"Change my password\">Change my password</a></div><div style=\"margin-top:5px;\">If the link above does not work, please copy & paste the url below in a web browser </div><div style=\"margin-top:5px;\">%s</div><div style=\"margin-top:5px;\">If you didn't request this, please ignore this email. This link will expire in 24 hours. </div></div></div>"; 
	public static final String MAIL_REGISTER_USER = "<div><img src=\"%s/gooru/images/mail/gooru.png\"></div><div style=\"font-family:tahoma;font-size:10pt;border:1px solid #E5E5E5;float:left;margin:5px 5px 5px 60px;padding:10px\"><img style=\"float:left\" src=\"%s/gooru/images/mail/school.png\">Gooru is a web experience for teacher that supports student curiosity, creativity and commitment by making learning fun and engaging inside and outside of the class. Gooru is a free web application that implements search and teach model through Classplan and Classroom.<div style=\"font-weight:bold;clear:left;padding-top:20px\">With reference to your invite request, we are pleased to inform you that your request has been processed successfully.</div><div style=\"margin-top:20px;border-bottom:1px solid #E5E5E5\"></div><div style=\"display:inline;\"><div style=\"padding-top:20px;float:left;font-weight:bold\">Click here to Register: </div><div style=\"float:left;margin:20px\">%s</div></div></div></div>";

}