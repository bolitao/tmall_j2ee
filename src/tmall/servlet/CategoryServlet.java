package tmall.servlet;

import com.sun.xml.internal.ws.api.message.Header;
import tmall.bean.Category;
import tmall.util.ImageUtil;
import tmall.util.Page;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Boli Tao
 */
@WebServlet(name = "CategoryServlet")
public class CategoryServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        Map<String, String> params = new HashMap<>();
        InputStream is = super.parseUpload(request, params);
        String name = params.get("name");
        Category c = new Category();
        c.setName(name);
        categoryDAO.add(c);
        File imageFolder = new File(request.getSession().getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, c.getId() + ".jpg");
        try {
            if (null != is && 0 != is.available()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] b = new byte[1024 * 1024];
                    int length = 0;
                    while (-1 != (length = is.read(b))) {
                        fos.write(b, 0, length);
                    }
                    fos.flush();
                    // transfer other image type to jpg
                    BufferedImage img = ImageUtil.change2jpg(file);
                    assert img != null;
                    ImageIO.write(img, "jpg", file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "@admin_category_list";
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        categoryDAO.delete(id);
        return "@admin_category_list";
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Category c = categoryDAO.get(id);
        request.setAttribute("c", c);
        return "admin/editCategory.jsp";
    }

    /**
     * 修改已存在的分类信息
     * FIXME: 修改分类图片后浏览器仍然从缓存中加载旧图片
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 跳转信息
     */
    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        Map<String, String> params = new HashMap<>();
        InputStream is = super.parseUpload(request, params);
        String name = params.get("name");
        int id = Integer.parseInt(params.get("id"));
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        categoryDAO.update(c);
        File imageFolder = new File(request.getSession().getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, c.getId() + ".jpg");
        boolean result = file.getParentFile().mkdirs();
        try {
            if (null != is && 0 != is.available()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] b = new byte[1024 * 1024];
                    int length = 0;
                    while (-1 != (length = is.read(b))) {
                        fos.write(b, 0, length);
                    }
                    fos.flush();
                    BufferedImage img = ImageUtil.change2jpg(file);
                    assert img != null;
                    ImageIO.write(img, "jpg", file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "@admin_category_list";
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        // get category data by sent in page's start and count
        List<Category> cs = categoryDAO.list(page.getStart(), page.getCount());
        int total = categoryDAO.getTotal();
        page.setTotal(total);
        request.setAttribute("thecs", cs);
        request.setAttribute("page", page);
        return "admin/listCategory.jsp";
    }
}
