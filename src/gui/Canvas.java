package gui;

import coordinator.Coordinator;
import logika.Igra;
import logika.Player;
import splosno.Poteza;

import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import javax.swing.JPanel;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    protected Igra game;

    protected int squareSize;
    protected static final int BORDER_BUFFER = 5;

    int boardStartX;
    int boardEndX;
    int boardStartY;
    int boardEndY;

    protected Color backgroundColour;
    protected Color lineColour;
    protected Stroke lineWidth;

    public Canvas(int x, int y) {
        this.setPreferredSize(new Dimension(x, y));

        this.game = null;

        this.backgroundColour = Color.GREEN;

        this.lineColour = Color.BLACK;
        this.lineWidth = new BasicStroke(2.0F);

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
    }

    private int round(double a) {
        return (int)Math.round(a);
    }

    public void setGame(Igra g) {
        this.game = g;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (this.game == null) return;
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;



        int h = getHeight();
        int w = getWidth();
        int c = Math.min(h, w)/2;
        squareSize = c/Math.max(game.sizeX, game.sizeY);
        boardStartX = c - game.sizeX/2 * squareSize;
        boardEndX = c + game.sizeX/2 * squareSize;
        boardStartY = c - game.sizeY/2 * squareSize;
        boardEndY = c + game.sizeY/2 * squareSize;

        g2.setColor(backgroundColour);

        g2.fillRect(boardStartX, boardStartY, game.sizeX* squareSize, game.sizeY* squareSize);

        g2.setColor(lineColour);
        g2.setStroke(this.lineWidth);

        for (int i = 0; i < game.sizeX+1; i++) {
            g2.drawLine(boardStartX, boardStartY + i* squareSize, boardEndX, boardStartY + i* squareSize);
        }
        for (int i = 0; i < game.sizeY+1; i++) {
            g2.drawLine(boardStartX + i* squareSize, boardStartY, boardStartX + i* squareSize, boardEndY);
        }

        for (int x = 0; x < game.sizeX; x++) {
            for (int y = 0; y < game.sizeY; y++) {
                Player p = game.getBoard()[x][y];
                if (p == null) {
                    continue;
                }
                g2.setColor(p.getColour());
                g2.fillOval(boardStartX + x * squareSize + BORDER_BUFFER, boardStartY + y * squareSize + BORDER_BUFFER,
                        squareSize - 2*BORDER_BUFFER, squareSize - 2*BORDER_BUFFER);
            }
        }

        g2.setColor(game.getPlayer().getColour());
        Set<Poteza> possible = game.getPossibleMoves().keySet();
        for (Poteza p: possible) {
            g2.drawOval(boardStartX + p.x() * squareSize + BORDER_BUFFER, boardStartY + p.y() * squareSize + BORDER_BUFFER,
                    squareSize - 2*BORDER_BUFFER, squareSize - 2*BORDER_BUFFER);
        }

        g2.setColor(Color.BLACK);
        g2.drawString(game.getPlayer().toString(), boardStartX, boardEndY + 5 * BORDER_BUFFER);
        g2.drawString(game.status.toString(), boardStartX + BORDER_BUFFER, boardEndY + 10 * BORDER_BUFFER);
    }

    private Poteza getPotezaFromCoord(int x, int y) {
        if (x < boardStartX || x > boardEndX || y < boardStartY || y > boardEndY) {
            return null;
        }
        int xField = (x - boardStartX) / squareSize;
        int yField = (y - boardStartY) / squareSize;
        return new Poteza(xField, yField);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.game == null) return;
        if (Coordinator.HumanTurn) {
            int x = e.getX();
            int y = e.getY();
            Coordinator.playHumanMove(getPotezaFromCoord(x, y));
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (this.game == null) return;

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (this.game == null) return;
        repaint();

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (this.game == null) return;

    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (this.game == null) return;

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (this.game == null) return;
        repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (this.game == null) return;

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (this.game == null) return;

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (this.game == null) return;
        int key = e.getKeyCode();
        Point loc = MouseInfo.getPointerInfo().getLocation();
        Point origin = this.getLocationOnScreen();
        int x = loc.x - origin.x;
        int y = loc.y - origin.y;
        Poteza poteza = getPotezaFromCoord(x, y);
        if (poteza == null) {return;}
        if (key == KeyEvent.VK_P) {
            game.player = game.swapPlayer(game.getPlayer());
            repaint();
            return;
        }
        Player p = switch (key) {
            case KeyEvent.VK_B -> Player.BLACK;
            case KeyEvent.VK_W -> Player.WHITE;
            default -> null;
        };
        if (p == null && e.getKeyCode() != KeyEvent.VK_X) {return;}
        game.getBoard()[poteza.x()][poteza.y()] = p;
        game.updateBoundary(poteza);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (this.game == null) return;
        repaint();
    }
}
