package views.tags.cms;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import controllers.cms.Profiler;

import models.cms.CMSPage;
import play.mvc.Router;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import play.templates.JavaExtensions;

@FastTags.Namespace("cms")
public class Tags extends FastTags {
	public static void _display(Map<?, ?> args, Closure body, PrintWriter out,
			ExecutableTemplate template, int fromLine) throws Throwable {

		String pageName = (String) args.get("arg");
		CMSPage page = CMSPage.findById(pageName);

    String safeBody = body != null ? JavaExtensions.toString(body) : "";

		if (page == null) {
			page = new CMSPage();
			page.name = pageName;
			page.title = "Fragment on "+template.template.name;
			page.body = safeBody;
			page.active = false;
			page.save();
			out.print(safeBody);
		} else if (!page.active) {
			out.print(safeBody);
		} else if (page.body != null) {
			out.print(page.body);
		}
		edit(out, page.name);
	}
	
	public static void _edit(Map<?, ?> args, Closure body, PrintWriter out,
			ExecutableTemplate template, int fromLine) throws Throwable {
		String pageName = (String) args.get("arg");
		edit(out, pageName);
	}

	
	private static void edit(PrintWriter out, String name) throws Throwable {
		if (!Profiler.canEdit(name))
			return;
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("pageName", name);
		out.print("<a class=\"cms-edit\" href=\"" + Router.reverse("cms.Admin.editPage", args) + "\">");
		out.print("<img alt=\"Edit\" src=\"/public/images/edit.gif\">");
		out.print("</a>");		
	}
}
