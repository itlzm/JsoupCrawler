package test;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * 通过Jsoup解析ZARA中国官网，获取产品根级分类，二级分类，以及三级分类
 * 在控制台输出产品分类列表层级关系
 * @author Administrator
 *
 */
public class Test {

	public static void main(String[] args) throws Exception {
		
		String str = "http://www.zara.cn/cn/";
		//通过Jsoup获取连接
		Connection conn = Jsoup.connect(str);
		//通过连接获取Document对象
		Document doc = conn.get();
		//获取id为menu的元素
		Element nav = doc.getElementById("menu");
		//获取该元素下第一个子元素
		Element nav_ul = nav.child(0);
		//获取该元素的所有子节点
		List<Node> nav_ul_Childs = nav_ul.childNodes();
		
		List<Node> nav_ul_lis = new ArrayList<Node>();
		for(Node node : nav_ul_Childs){
			//去除掉nav_ul_Childs元素中子节点大于1
			//且子节点中第一个节点a标签内容为"Join Life"和"+ 资讯"的子节点
			if(node.childNodeSize()>1){
				Node li_a = node.childNode(0);
				Element a = (Element) li_a;
				String text = a.text();
				if(!text.equals("Join Life")&&!text.equals("+ 资讯")){
					nav_ul_lis.add(node);
				}
			}
		}
		for(Node node : nav_ul_lis){
			Node nav_ul_lis_a = node.childNode(0);
			Node nav_ul_lis_ul = node.childNode(1);
			Element a = (Element) nav_ul_lis_a;
			Element ul = (Element) nav_ul_lis_ul;
			String text = a.text();
			String href = a.attr("href");
			//获取nav_ul_lis_ul的所有子节点，包括li标签，i标签等
			Elements nav_ul_lis_ul_childs = ul.children();
			//创建集合，用于存储nav_ul_lis_ul节点下，所有标签名为li的子节点
			List<Element> nav_ul_lis_ul_lis = new ArrayList<Element>();
			for(Element e : nav_ul_lis_ul_childs){
				if(e.tagName().equals("li")){
					nav_ul_lis_ul_lis.add(e);
				}
			}
			//输出根级分类
			System.out.println(href+"----"+text+"----size:"+nav_ul_lis_ul_lis.size());
			/*
			 * nav_ul_lis_ul_lis下每个li表示一个二级分类
			 * li下第一个节点为a标签，存储二级分类的href和text
			 * 可能包含三级分类，通过li的size判断是否存在三级分类
			 */
			for(Element e : nav_ul_lis_ul_lis){
				Element nav_ul_lis_ul_lis_a = e.child(0);
				String href1 = nav_ul_lis_ul_lis_a.attr("href");
				String text1 = nav_ul_lis_ul_lis_a.text();
				//输出二级分类
				System.out.println("\t"+href1+"----"+text1+"----size:"+e.childNodeSize());
				/*
				 * 如果存在三级分类,该分类下包含有li标签和i标签,
				 * 只获取li标签后,得到li标签下的a标签，即可获取三级分类的href和text
				 */
				if(e.childNodeSize()>1){
					Element nav_ul_lis_ul_lis_ul = e.child(1);
					Elements nav_ul_lis_ul_lis_ul_childs = nav_ul_lis_ul_lis_ul.children();
					for(Element e1 : nav_ul_lis_ul_lis_ul_childs){
						if(e1.tagName().equals("li")){
							Element nav_ul_lis_ul_lis_ul_lis_a = e1.child(0);
							String href2 = nav_ul_lis_ul_lis_ul_lis_a.attr("href");
							String text2 = nav_ul_lis_ul_lis_ul_lis_a.text();
							//输出三级分类
							System.out.println("\t"+"\t"+href2+"----"+text2);
						}
					}
				}
			}
			System.out.println("\n");
		}
	}
}
