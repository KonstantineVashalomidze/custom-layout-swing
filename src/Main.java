import javax.swing.*;
import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        new CircleLayoutFrame().setVisible(true);
    }
}



class CircleLayout
    implements LayoutManager
{
    private int minWidth = 0;
    private int minHeight = 0;
    private int preferredWidth = 0;
    private int preferredHeight = 0;
    private boolean sizesSet = false;
    private int maxComponentWidth = 0;
    private int maxComponentHeight = 0;


    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        this.setSizes(parent);
        Insets insets = parent.getInsets();
        int width = this.preferredWidth + insets.left + insets.right;
        int height = this.preferredHeight + insets.top + insets.bottom;
        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {

        this.setSizes(parent);
        Insets insets = parent.getInsets();
        int width = this.minWidth + insets.left + insets.right;
        int height = this.minHeight + insets.top + insets.bottom;
        return new Dimension(width, height);
    }

    @Override
    public void layoutContainer(Container parent) {
        this.setSizes(parent);

        // Compute center of the circle

        Insets insets = parent.getInsets();
        int containerWidth = parent.getSize().width - insets.left - insets.right;
        int containerHeight = parent.getSize().height - insets.top - insets.bottom;

        int xcenter = insets.left + containerWidth / 2;
        int ycenter = insets.top + containerHeight / 2;


        // Compute radius of the circle
        int xradius = (containerWidth - this.maxComponentWidth) / 2;
        int yradius = (containerHeight - this.maxComponentHeight) / 2;
        int radius = Math.min(xradius, yradius);


        // Lay out components along the circle
        int n = parent.getComponentCount();
        for (int i = 0; i < n; i++)
        {
            Component c = parent.getComponent(i);
            if (c.isVisible())
            {
                double angle = 2 * Math.PI * i / n;

                // center point of component
                int x = xcenter + (int) (Math.cos(angle) * radius);
                int y = ycenter + (int) (Math.sin(angle) * radius);

                /*
                * Move component so that its center is (x, y)
                * and its size is its preferred size
                * */
                Dimension d = c.getPreferredSize();
                c.setBounds(x - d.width / 2, y - d.height / 2, d.width, d.height);


            }
        }

    }

    public void setSizes(Container parent)
    {
        if (this.sizesSet) return;
        int n = parent.getComponentCount();

        this.preferredWidth = 0;
        this.preferredHeight = 0;
        this.minWidth = 0;
        this.minHeight = 0;
        this.maxComponentWidth = 0;
        this.maxComponentHeight = 0;


        /*
        * Compute the maximum component widths and heights
        * and set the preferred size to the sum of the component sizes
        * */

        for (int i = 0; i < n; i++)
        {
            Component c = parent.getComponent(i);
            if (c.isVisible())
            {
                Dimension d = c.getPreferredSize();
                this.maxComponentWidth = Math.max(this.maxComponentWidth, d.width);
                this.maxComponentHeight = Math.max(this.maxComponentHeight, d.height);
                this.preferredWidth = d.width;
                this.preferredHeight = d.height;
            }
        }

        this.minWidth = this.preferredWidth / 2;
        this.minHeight = this.preferredHeight / 2;
        this.sizesSet = true;

    }






}


class CircleLayoutFrame
    extends JFrame
{
    public CircleLayoutFrame()
    {
        this.setLayout(new CircleLayout());
        for (int i = 0; i < 1000; i++) {
            this.add(new JButton(String.valueOf(i)));
        }
        this.pack();
    }
}

