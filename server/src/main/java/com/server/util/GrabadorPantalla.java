package com.server.util;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GrabadorPantalla {

    private FFmpegFrameRecorder grabador;
    private boolean grabando;
    private Java2DFrameConverter conversorFrame;
    private Robot robot;
    private Rectangle pantalla;
    private int fpsOriginal;
    private String rutaArchivo;

    public GrabadorPantalla(String nombreVideo, int fpsOriginal, int fpsSalida) throws Exception {
        this.fpsOriginal = fpsOriginal;
        this.rutaArchivo = "src/main/resources/videos/" + nombreVideo;

        pantalla = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        conversorFrame = new Java2DFrameConverter();
        robot = new Robot();

        // Validar y preparar la ruta
        File archivoSalida = new File(rutaArchivo);
        File directorioPadre = archivoSalida.getParentFile();
        if (!directorioPadre.exists() && !directorioPadre.mkdirs()) {
            throw new Exception("No se pudo crear el directorio para guardar el video: " + directorioPadre.getAbsolutePath());
        }

        // Configuración del grabador
        grabador = new FFmpegFrameRecorder(rutaArchivo, pantalla.width, pantalla.height);
        grabador.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        grabador.setFormat("mp4");
        grabador.setFrameRate(fpsSalida);
        grabador.setVideoBitrate(5000000);
        grabador.setPixelFormat(0); // Formato de píxel YUV420P
    }

    public void iniciarGrabacion() {
        try {
            grabador.start(); // Aquí se lanza FFmpegFrameRecorder.Exception
            grabando = true;
            comenzarCaptura();
        } catch (FFmpegFrameRecorder.Exception e) {
            System.err.println("Error al iniciar la grabación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void comenzarCaptura() {
        new Thread(() -> {
            try {
                while (grabando) {
                    BufferedImage captura = robot.createScreenCapture(pantalla);
                    Frame frame = conversorFrame.convert(captura);
                    grabador.record(frame);
                    Thread.sleep(1000 / fpsOriginal);
                }
            } catch (Exception e) {
                System.err.println("Error durante la grabación: " + e.getMessage());
                detenerGrabacionSilenciosa();
            }
        }).start();
    }

    public void detenerGrabacion() {
        grabando = false;
        if (grabador != null) {
            try {
                grabador.stop();
                System.out.println("Video guardado en: " + rutaArchivo);
            } catch (Exception e) {
                System.err.println("Error al detener el grabador: " + e.getMessage());
            } finally {
                try {
                    grabador.release();
                } catch (org.bytedeco.javacv.FFmpegFrameRecorder.Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void detenerGrabacionSilenciosa() {
        grabando = false;
        if (grabador != null) {
            try {
                grabador.stop();
            } catch (Exception ignored) {
            } finally {
                try {
                    grabador.release();
                } catch (org.bytedeco.javacv.FFmpegFrameRecorder.Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }
}
