/***
  * Author:        Paul Rodriguez
  * Created:       3/11/2014
  * Last Updated:  3/13/2014
  */
import java.util.Comparator;
import java.util.ArrayList;
public class KdTree
{
    private static class Node
    {
        private Point2D point;
        private RectHV rect;
        private boolean isVertical;
        private Node left;
        private Node right;
        
        public Node(Point2D p, boolean vertical, Node Left, Node Right, RectHV rectangle)
        {
            this.point = p;
            this.isVertical = vertical;
            this.left = Left;
            this.right = Right;
            this.rect = rectangle;
        }
        
        public RectHV getRect()
        {
            return this.rect;
        }
        public Point2D getPoint()
        {
            return point;
        }
        
        public boolean vertical()
        {
            return isVertical;
        }
        
        public Node getLeft()
        {
            return left;
        }
        
        public Node getRight()
        {
            return right;
        }
        
        public void setLeftNode(Node leftNode)
        {
            this.left = leftNode;
        }
        
        public void setRightNode(Node rightNode)
        {
            this.right = rightNode;
        }
    }
    
    private Node root;
    private int size;
    public KdTree()
    {
        root = null;
        size = 0;
    }
    
    public boolean isEmpty()
    {
        return root == null;
    }
    
    public int size()
    {
        return size;
    }
    
    public void insert(Point2D p)
    {
        //  base case: empty tree
        if (size == 0)
        {
            RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            root = new Node(p, true, null, null, rect);
            size++;
            return;
        }
        
            
        //StdOut.println("root is not null");
        //  point at root initially
        Node n = root;
       
        Comparator<Point2D> comparator = Point2D.Y_ORDER;
        
        while (n != null)
        {
            //  return if point is already in the tree
            if (n.getPoint().equals(p))
                return;
            //  if we are at a vertical node
            if (n.vertical())
            {
                comparator = Point2D.X_ORDER;
            }
            else
            {
                comparator = Point2D.Y_ORDER;
            }
            //  go to the left if point left to vertical point or below a horizontal point
            if (comparator.compare(p, n.getPoint()) < 0)
            {
                //  if the left point is null then create new node and set it
                if(n.getLeft() == null)
                {
                    RectHV rect = null;
                    if (n.vertical())
                    {
                        //  point to left of current point
                       rect = new RectHV(n.getRect().xmin(), n.getRect().ymin(), n.getPoint().x(), n.getRect().ymax());
                    }
                    else
                    {
                        //  point at bottom of current point
                        rect = new RectHV(n.getRect().xmin(), n.getRect().ymin(), n.getRect().xmax(), n.getPoint().y());
                    }
                    //  create new node to be inserted to 
                    Node leftNode = new Node(p, !n.vertical(), null, null, rect);
                    n.setLeftNode(leftNode);
                    size++;
                    break;
                }
                
                n = n.getLeft();
            }
            else
            {
                //  reached end so insert new node to right
                if (n.getRight() == null)
                {
                    RectHV rect = null;
                    
                    if (n.vertical())
                    {
                        //  right to vertical point
                       rect = new RectHV(n.getPoint().x(), n.getRect().ymin(), n.getRect().xmax(), n.getRect().ymax());
                    }
                    else
                    {
                        //  top of horizontal point
                        rect = new RectHV(n.getRect().xmin(), n.getPoint().y(), n.getRect().xmax(), n.getRect().ymax());
                    }
                    Node rightNode = new Node(p, !n.vertical(), null, null, rect);
                    n.setRightNode(rightNode);
                    size++;
                    break;
                }
                    
                n = n.getRight();
            }
        }  
    }
    
    public boolean contains(Point2D p)
    {
        if (root == null)
            return false;
        
        Node iterator = root;
        
        Comparator<Point2D> comparator = Point2D.Y_ORDER;
        
        while (iterator != null)
        {
            Point2D ipoint = iterator.getPoint();
            if (ipoint.equals(p))
                return true;
            
            if (iterator.vertical())
            {
                comparator = Point2D.X_ORDER;
            }
            else
            {
                comparator = Point2D.Y_ORDER;
            }
            
            //  go left
            if (comparator.compare(ipoint, p) < 0)
            {
                iterator = iterator.getLeft();
            }
            else
            {
                iterator = iterator.getRight();
            }
        }
        
        return false;
    }
    
public void draw() {
    //  draw the black box points are supposed to be in
    StdDraw.setPenColor(StdDraw.BLACK);
    Point2D b0 = new Point2D(0.0, 0.0);
    Point2D b1 = new Point2D(1.0, 0.0);
    b0.drawTo(b1);
    Point2D l0 = new Point2D(0.0, 0.0);
    Point2D l1 = new Point2D(0.0, 1.0);
    l0.drawTo(l1);
    Point2D t0 = new Point2D(0.0, 1.0);
    Point2D t1 = new Point2D(1.0, 1.0);
    t0.drawTo(t1);
    Point2D r0 = new Point2D(1.0, 0.0);
    Point2D r1 = new Point2D(1.0, 1.0);
    r0.drawTo(r1);
    drawRecursive(root);
}

private void drawRecursive(Node node) {
    if (node == null) {
        return;
    }
    RectHV rect = node.getRect();
    Point2D point = node.getPoint();
    
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    point.draw();
    
    if (node.vertical()) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        
        Point2D bottom = new Point2D(point.x(), rect.ymin());
        Point2D top = new Point2D(point.x(), rect.ymax());
        bottom.drawTo(top);
        
        drawRecursive(node.getLeft());
        drawRecursive(node.getRight());
    } 
    else
    {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();
        
        Point2D left = new Point2D(rect.xmin(), point.y());
        Point2D right = new Point2D(rect.xmax(), point.y());
        left.drawTo(right);
        
        drawRecursive(node.getLeft());
        drawRecursive(node.getRight());
    }
}
    
    //  TODO
     public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> pointsInRange = new ArrayList<Point2D>();
        rangeRecursive(pointsInRange, rect, root);
        return pointsInRange;
    }
     
     private void rangeRecursive(ArrayList<Point2D> rangeList, RectHV rect, Node n)
     {
         if (n == null)
             return;
         
         Point2D p = n.getPoint();
         if (rect.contains(p))
             rangeList.add(p);
         
         double pointCoord = p.y();
         double rectMin = rect.ymin();
         double rectMax = rect.ymax();
         if (n.vertical())
         {
             pointCoord = p.x();
             rectMin = rect.xmin();
             rectMax = rect.xmax();
         }
         
         if (pointCoord > rectMin)
             rangeRecursive(rangeList, rect, n.getLeft());
         if (pointCoord <= rectMax)
             rangeRecursive(rangeList, rect, n.getRight());
     }
     
     //  TODO
     public Point2D nearest(Point2D p)
     {
         MinPQ<Point2D> minpt = new MinPQ<Point2D>();
         nearestRecursive();
         return minpt;
     }
     
     private v
}
