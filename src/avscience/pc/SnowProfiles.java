
package avscience.pc;

import java.awt.Graphics;

public class SnowProfiles
{

    public SnowProfiles(Graphics g1)
    {
        yoffset = 2;
        xoffset = 2;
        g = g1;
    }

    public void drawProfile(int i)
    {
        xoffset = 2;
        yoffset = 2;
        int ai[][] = getPoints(i);
        if(ai != null)
        {
            int ai1[] = ai[0];
            int ai2[] = ai[1];
            int j = ai1.length;
            g.fillPolygon(ai1, ai2, j);
        }
    }

    public void drawProfile(int i, int j, int k)
    {
        xoffset = 2 + j;
        yoffset = 2 + k;
        int ai[][] = getPoints(i);
        if(ai != null)
        {
            int ai1[] = ai[0];
            int ai2[] = ai[1];
            int l = ai1.length;
            g.fillPolygon(ai1, ai2, l);
        }
    }

    public int[][] getPoints(int i)
    {
        int ai[][] = (int[][])null;
        switch(i)
        {
        case 1: // '\001'
            ai = new int[2][4];
            ai[0][0] = xoffset + 8;
            ai[1][0] = yoffset + 40;
            ai[0][1] = xoffset + 12;
            ai[1][1] = yoffset + 40;
            ai[0][2] = xoffset + 12;
            ai[1][2] = yoffset + 0;
            ai[0][3] = xoffset + 8;
            ai[1][3] = yoffset + 0;
            break;

        case 2: // '\002'
            ai = new int[2][14];
            ai[0][0] = xoffset + 16;
            ai[1][0] = yoffset + 40;
            ai[0][1] = xoffset + 20;
            ai[1][1] = yoffset + 40;
            ai[0][2] = xoffset + 20;
            ai[1][2] = yoffset + 0;
            ai[0][3] = xoffset + 16;
            ai[1][3] = yoffset + 0;
            ai[0][4] = xoffset + 16;
            ai[1][4] = yoffset + 8;
            ai[0][5] = xoffset + 0;
            ai[1][5] = yoffset + 8;
            ai[0][6] = xoffset + 0;
            ai[1][6] = yoffset + 16;
            ai[0][7] = xoffset + 8;
            ai[1][7] = yoffset + 16;
            ai[0][8] = xoffset + 8;
            ai[1][8] = yoffset + 24;
            ai[0][9] = xoffset + 12;
            ai[1][9] = yoffset + 24;
            ai[0][10] = xoffset + 12;
            ai[1][10] = yoffset + 32;
            ai[0][11] = xoffset + 16;
            ai[1][11] = yoffset + 32;
            ai[0][12] = xoffset + 16;
            ai[1][12] = yoffset + 40;
            ai[0][13] = xoffset + 20;
            ai[1][13] = yoffset + 40;
            break;

        case 3: // '\003'
            ai = new int[2][16];
            ai[0][0] = xoffset + 20;
            ai[1][0] = yoffset + 0;
            ai[0][1] = xoffset + 16;
            ai[1][1] = yoffset + 0;
            ai[0][2] = xoffset + 16;
            ai[1][2] = yoffset + 8;
            ai[0][3] = xoffset + 12;
            ai[1][3] = yoffset + 8;
            ai[0][4] = xoffset + 12;
            ai[1][4] = yoffset + 12;
            ai[0][5] = xoffset + 8;
            ai[1][5] = yoffset + 12;
            ai[0][6] = xoffset + 8;
            ai[1][6] = yoffset + 16;
            ai[0][7] = xoffset + 0;
            ai[1][7] = yoffset + 16;
            ai[0][8] = xoffset + 0;
            ai[1][8] = yoffset + 24;
            ai[0][9] = xoffset + 8;
            ai[1][9] = yoffset + 24;
            ai[0][10] = xoffset + 8;
            ai[1][10] = yoffset + 28;
            ai[0][11] = xoffset + 12;
            ai[1][11] = yoffset + 28;
            ai[0][12] = xoffset + 12;
            ai[1][12] = yoffset + 32;
            ai[0][13] = xoffset + 16;
            ai[1][13] = yoffset + 32;
            ai[0][14] = xoffset + 16;
            ai[1][14] = yoffset + 40;
            ai[0][15] = xoffset + 20;
            ai[1][15] = yoffset + 40;
            break;

        case 4: // '\004'
            ai = new int[2][12];
            ai[0][0] = xoffset + 16;
            ai[1][0] = yoffset + 40;
            ai[0][1] = xoffset + 20;
            ai[1][1] = yoffset + 40;
            ai[0][2] = xoffset + 20;
            ai[1][2] = yoffset + 0;
            ai[0][3] = xoffset + 16;
            ai[1][3] = yoffset + 0;
            ai[0][4] = xoffset + 16;
            ai[1][4] = yoffset + 8;
            ai[0][5] = xoffset + 12;
            ai[1][5] = yoffset + 8;
            ai[0][6] = xoffset + 12;
            ai[1][6] = yoffset + 16;
            ai[0][7] = xoffset + 8;
            ai[1][7] = yoffset + 16;
            ai[0][8] = xoffset + 8;
            ai[1][8] = yoffset + 24;
            ai[0][9] = xoffset + 4;
            ai[1][9] = yoffset + 24;
            ai[0][10] = xoffset + 4;
            ai[1][10] = yoffset + 32;
            ai[0][11] = xoffset + 16;
            ai[1][11] = yoffset + 32;
            break;

        case 5: // '\005'
            ai = new int[2][6];
            ai[0][0] = xoffset + 16;
            ai[1][0] = yoffset + 40;
            ai[0][1] = xoffset + 20;
            ai[1][1] = yoffset + 40;
            ai[0][2] = xoffset + 20;
            ai[1][2] = yoffset + 0;
            ai[0][3] = xoffset + 0;
            ai[1][3] = yoffset + 0;
            ai[0][4] = xoffset + 0;
            ai[1][4] = yoffset + 20;
            ai[0][5] = xoffset + 16;
            ai[1][5] = yoffset + 20;
            break;

        case 6: // '\006'
            ai = new int[2][12];
            ai[0][0] = xoffset + 0;
            ai[1][0] = yoffset + 40;
            ai[0][1] = xoffset + 20;
            ai[1][1] = yoffset + 40;
            ai[0][2] = xoffset + 20;
            ai[1][2] = yoffset + 0;
            ai[0][3] = xoffset + 16;
            ai[1][3] = yoffset + 0;
            ai[0][4] = xoffset + 16;
            ai[1][4] = yoffset + 8;
            ai[0][5] = xoffset + 12;
            ai[1][5] = yoffset + 8;
            ai[0][6] = xoffset + 12;
            ai[1][6] = yoffset + 16;
            ai[0][7] = xoffset + 8;
            ai[1][7] = yoffset + 16;
            ai[0][8] = xoffset + 8;
            ai[1][8] = yoffset + 24;
            ai[0][9] = xoffset + 4;
            ai[1][9] = yoffset + 24;
            ai[0][10] = xoffset + 4;
            ai[1][10] = yoffset + 32;
            ai[0][11] = xoffset + 0;
            ai[1][11] = yoffset + 32;
            break;

        case 7: // '\007'
            ai = new int[2][18];
            ai[0][0] = xoffset + 0;
            ai[1][0] = yoffset + 40;
            ai[0][1] = xoffset + 20;
            ai[1][1] = yoffset + 40;
            ai[0][2] = xoffset + 20;
            ai[1][2] = yoffset + 0;
            ai[0][3] = xoffset + 16;
            ai[1][3] = yoffset + 0;
            ai[0][4] = xoffset + 16;
            ai[1][4] = yoffset + 8;
            ai[0][5] = xoffset + 12;
            ai[1][5] = yoffset + 8;
            ai[0][6] = xoffset + 12;
            ai[1][6] = yoffset + 12;
            ai[0][7] = xoffset + 8;
            ai[1][7] = yoffset + 12;
            ai[0][8] = xoffset + 8;
            ai[1][8] = yoffset + 16;
            ai[0][9] = xoffset + 4;
            ai[1][9] = yoffset + 16;
            ai[0][10] = xoffset + 4;
            ai[1][10] = yoffset + 20;
            ai[0][11] = xoffset + 16;
            ai[1][11] = yoffset + 20;
            ai[0][12] = xoffset + 16;
            ai[1][12] = yoffset + 24;
            ai[0][13] = xoffset + 12;
            ai[1][13] = yoffset + 24;
            ai[0][14] = xoffset + 12;
            ai[1][14] = yoffset + 28;
            ai[0][15] = xoffset + 8;
            ai[1][15] = yoffset + 28;
            ai[0][16] = xoffset + 8;
            ai[1][16] = yoffset + 32;
            ai[0][17] = xoffset + 0;
            ai[1][17] = yoffset + 32;
            break;

        case 8: // '\b'
            ai = new int[2][6];
            ai[0][0] = xoffset + 0;
            ai[1][0] = yoffset + 40;
            ai[0][1] = xoffset + 20;
            ai[1][1] = yoffset + 40;
            ai[0][2] = xoffset + 20;
            ai[1][2] = yoffset + 0;
            ai[0][3] = xoffset + 16;
            ai[1][3] = yoffset + 0;
            ai[0][4] = xoffset + 16;
            ai[1][4] = yoffset + 20;
            ai[0][5] = xoffset + 0;
            ai[1][5] = yoffset + 20;
            break;

        case 9: // '\t'
            ai = new int[2][12];
            ai[0][0] = xoffset + 0;
            ai[1][0] = yoffset + 40;
            ai[0][1] = xoffset + 20;
            ai[1][1] = yoffset + 40;
            ai[0][2] = xoffset + 20;
            ai[1][2] = yoffset + 0;
            ai[0][3] = xoffset + 0;
            ai[1][3] = yoffset + 0;
            ai[0][4] = xoffset + 0;
            ai[1][4] = yoffset + 8;
            ai[0][5] = xoffset + 8;
            ai[1][5] = yoffset + 8;
            ai[0][6] = xoffset + 8;
            ai[1][6] = yoffset + 12;
            ai[0][7] = xoffset + 16;
            ai[1][7] = yoffset + 12;
            ai[0][8] = xoffset + 16;
            ai[1][8] = yoffset + 28;
            ai[0][9] = xoffset + 8;
            ai[1][9] = yoffset + 28;
            ai[0][10] = xoffset + 8;
            ai[1][10] = yoffset + 32;
            ai[0][11] = xoffset + 0;
            ai[1][11] = yoffset + 32;
            break;

        case 10: // '\n'
            ai = new int[2][4];
            ai[0][0] = xoffset + 0;
            ai[1][0] = yoffset + 40;
            ai[0][1] = xoffset + 20;
            ai[1][1] = yoffset + 40;
            ai[0][2] = xoffset + 20;
            ai[1][2] = yoffset + 0;
            ai[0][3] = xoffset + 0;
            ai[1][3] = yoffset + 0;
            break;
        }
        return ai;
    }

    private Graphics g;
    private static final int scale = 4;
    private int yoffset;
    private int xoffset;
    private static final int xx = 2;
    private static final int yy = 2;
}
