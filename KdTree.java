/***
  * Author:        Paul Rodriguez
  * Created:       3/11/2014
  * Last Updated:  3/14/2014
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
        
        private Node(Point2D p, boolean vertical, Node left, Node right, RectHV rectangle)
        {
            this.point = p;
            this.isVertical = vertical;
            this.left = left;
            this.right = right;
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
                if (n.getLeft() == null)
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
        return containsRecursive(root, p);
    }
    
     private boolean containsRecursive(Node node, Point2D point) {
        if (node == null) 
        {
            return false;
        }

        Point2D nPoint = node.getPoint();
        if (nPoint.equals(point)) 
        {
            return true;
        }

        Comparator<Point2D> comparator = null;
        if (node.vertical()) {
            comparator = Point2D.X_ORDER;
        }
        else
        {
            comparator = Point2D.Y_ORDER;
        }

        if (comparator.compare(point, nPoint) < 0) 
        {
            return containsRecursive(node.getLeft(), point);
        } 
        else 
        {
            return containsRecursive(node.getRight(), point);
        }
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
     
     
     private Comparator<Point2D> distanceOrder(final Point2D queryPoint) {
        return new Comparator<Point2D>() {
            public int compare(Point2D p1, Point2D p2) {
                double dist1 = queryPoint.distanceSquaredTo(p1);
                double dist2 = queryPoint.distanceSquaredTo(p2);
                if (dist1 < dist2) return -1;
                else if (dist1 > dist2) return +1;
                else return 0;
            }
        };
    }
  
     public Point2D nearest(Point2D p)
     {
         if (size == 0)
             return null;
         
         MinPQ<Point2D> minpt = new MinPQ<Point2D>(distanceOrder(p));
         nearestRecursive(minpt, root, p, new RectHV(0.0, 0.0, 1.0, 1.0));
         return minpt.min();
     }
     
   
     private void nearestRecursive(MinPQ<Point2D> m, Node n, Point2D p, RectHV rect)
     {
         if (n == null)
             return;
         
         Point2D nPoint = n.getPoint();
         m.insert(nPoint);
         //  this is so that it doesnt make too much calls to  methods of Point2D and RectHV
         double rectXmin = rect.xmin();
         double rectXmax = rect.xmax();
         double rectYmin = rect.ymin();
         double rectYmax = rect.ymax();
         double pointx = nPoint.x();
         double pointy = nPoint.y();
         
         if (n.vertical())
         {
            RectHV leftRect = new RectHV(rectXmin, rectYmin,
                    pointx, rectYmax);
            RectHV rightRect = new RectHV(pointx, rectYmin,
                    rectXmax, rectYmax);


             if (p.x() < pointx)
             {
                 nearestRecursive(m, n.getLeft(), p, leftRect);
                 double distanceToNearest = m.min().distanceSquaredTo(p);
                 double distanceToRect = rightRect.distanceSquaredTo(p);
                 if (distanceToRect < distanceToNearest) 
                 {
                     nearestRecursive(m, n.getRight(), p, rightRect);
                 }
                 /*Node nRight = n.getRight();
                 if (nRight != null)
                 {
                     //  if distance to other rectangle is less than current min ditance point
                     if (nRight.getRect().distanceSquaredTo(p) < m.min().distanceSquaredTo(p))
                         nearestRecursive(m, n.getRight(), p);
                 }*/
             }
             else
             {
                 nearestRecursive(m, n.getRight(), p, rightRect);
                 double distanceToNearest = m.min().distanceSquaredTo(p);
                 double distanceToRect = leftRect.distanceSquaredTo(p);
                 if (distanceToRect < distanceToNearest) 
                 {
                     nearestRecursive(m, n.getLeft(), p, leftRect);
                 }
                 /*Node nLeft = n.getLeft();
                 if(nLeft != null)
                 {
                     //  if distance to other rectangle is less than current min ditance point
                     if (nLeft.getRect().distanceSquaredTo(p) < m.min().distanceSquaredTo(p))
                         nearestRecursive(m, n.getLeft(), p);
                 }*/
             }
         }
         else
         {
             //  for horizontal lines
             
             RectHV bottomRect = new RectHV(rectXmin, rectYmin, rectXmax, pointy);
             RectHV topRect = new RectHV(rectXmin, pointy, rectXmax, rectYmax);
             //  if point lies below the horizonal line of current search point
             if (p.y() < pointy)
             {
                 nearestRecursive(m, n.getLeft(), p, bottomRect);
                 double distanceToNearest = m.min().distanceSquaredTo(p);
                 double distanceToRect = topRect.distanceSquaredTo(p);
                 if (distanceToRect < distanceToNearest)
                 {
                     nearestRecursive(m, n.getRight(), p, topRect);
                 }
                 /*
                 Node nBottom = n.getLeft();
                 if (nBottom != null)
                 {
                     if (nBottom.getRect().distanceSquaredTo(p) < m.min().distanceSquaredTo(p))
                         nearestRecursive(m, n.getRight(), p);
                 }*/
             }
             else
             {
                 nearestRecursive(m, n.getRight(), p, topRect);
                 double distanceToNearest = m.min().distanceSquaredTo(p);
                 double distanceToRect = bottomRect.distanceSquaredTo(p);
                 if (distanceToRect < distanceToNearest)
                 {
                     nearestRecursive(m, n.getLeft(), p, bottomRect);
                 }
                 /*
                 Node nTop = n.getRight();
                 if (nTop != null)
                 {
                     if (nTop.getRect().distanceSquaredTo(p) < m.min().distanceSquaredTo(p))
                         nearestRecursive(m, n.getLeft(), p);
                 }*/
             }
         }
     }
}
