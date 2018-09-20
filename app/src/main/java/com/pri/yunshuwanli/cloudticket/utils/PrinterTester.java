package com.pri.yunshuwanli.cloudticket.utils;

import android.graphics.Bitmap;

import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.dal.exceptions.PrinterDevException;

import yswl.com.klibrary.util.L;

public class PrinterTester {

    private static PrinterTester printerTester;
    private IPrinter printer;

    private PrinterTester() {
    }

    public static PrinterTester getInstance() {
        if (printerTester == null) {
            printerTester = new PrinterTester();
        }
        return printerTester;
    }

    public void init(IDAL idal) {
        try {
            printer = idal.getPrinter();
            printer.init();

            L.i("init");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("init:"+e.toString());
        }
    }

    public String getStatus() {
        try {
            int status = printer.getStatus();
            L.i("getStatus");
            return statusCode2Str(status);
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("getStatus:"+e.toString());
            return "";
        }

    }

    public void fontSet(EFontTypeAscii asciiFontType, EFontTypeExtCode cFontType) {
        try {
            printer.fontSet(asciiFontType, cFontType);
            L.i("fontSet");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("fontSet:"+e.toString());
        }

    }

    public void spaceSet(byte wordSpace, byte lineSpace) {
        try {
            printer.spaceSet(wordSpace, lineSpace);
            L.i("spaceSet");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("spaceSet:"+e.toString());
        }
    }

    public void printStr(String str, String charset) {
        try {
            printer.printStr(str, charset);
            L.i("printStr");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("printStr:"+e.toString());
        }

    }

    public void step(int b) {
        try {
            printer.step(b);
            L.i("setStep");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("setStep:"+e.toString());
        }
    }

    public void printBitmap(Bitmap bitmap) {
        try {
            printer.printBitmap(bitmap);
            L.i("printBitmap");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("printBitmap:"+e.toString());
        }
    }

    public String start() {
        try {
            int res = printer.start();
            L.i("start");
            return statusCode2Str(res);
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("start:"+e.toString());
            return "";
        }

    }

    public void leftIndents(short indent) {
        try {
            printer.leftIndent(indent);
            L.i("leftIndent");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("leftIndent:"+e.toString());
        }
    }

    public int getDotLine() {
        try {
            int dotLine = printer.getDotLine();
            L.i("getDotLine");
            return dotLine;
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("getDotLine:"+e.toString());

            return -2;
        }
    }

    public void setGray(int level) {
        try {
            printer.setGray(level);
            L.i("setGray");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("setGray:"+ e.toString());
        }

    }

    public void setDoubleWidth(boolean isAscDouble, boolean isLocalDouble) {
        try {
            printer.doubleWidth(isAscDouble, isLocalDouble);
            L.i("doubleWidth");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("doubleWidth:"+ e.toString());
        }
    }

    public void setDoubleHeight(boolean isAscDouble, boolean isLocalDouble) {
        try {
            printer.doubleHeight(isAscDouble, isLocalDouble);
            L.i("doubleHeight");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("doubleHeight:"+ e.toString());
        }

    }

    public void setInvert(boolean isInvert) {
        try {
            printer.invert(isInvert);
            L.i("setInvert");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("setInvert:"+ e.toString());
        }

    }

    public String cutPaper(int mode) {
        try {
            printer.cutPaper(mode);
            L.i("cutPaper");
            return "cut paper successful";
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("cutPaper:"+e.toString());
            return e.toString();
        }
    }

    public String getCutMode() {
        String resultStr = "";
        try {
            int mode = printer.getCutMode();
            L.i("getCutMode");
            switch (mode) {
                case 0:
                    resultStr = "Only support full paper cut";
                    break;
                case 1:
                    resultStr = "Only support partial paper cutting ";
                    break;
                case 2:
                    resultStr = "support partial paper and full paper cutting ";
                    break;
                case -1:
                    resultStr = "No cutting knife,not support";
                    break;
                default:
                    break;
            }
            return resultStr;
        } catch (PrinterDevException e) {
            e.printStackTrace();
            L.e("getCutMode:"+ e.toString());
            return e.toString();
        }
    }

    public String statusCode2Str(int status) {
        String res = "";
        switch (status) {
            case 0:
                res = "Success ";
                break;
            case 1:
                res = "Printer is busy ";
                break;
            case 2:
                res = "Out of paper ";
                break;
            case 3:
                res = "The format of print data packet error ";
                break;
            case 4:
                res = "Printer malfunctions ";
                break;
            case 8:
                res = "Printer over heats ";
                break;
            case 9:
                res = "Printer voltage is too low";
                break;
            case 240:
                res = "Printing is unfinished ";
                break;
            case 252:
                res = " The printer has not installed font library ";
                break;
            case 254:
                res = "Data package is too long ";
                break;
            default:
                break;
        }
        return res;
    }
}
