import ScalateKeys._

seq(scalateSettings:_*)

// Scalate Precompilation and Bindings
scalateTemplateConfig in Compile <<= (sourceDirectory in Compile) { base =>
  Seq(
    TemplateConfig(
      base / "views",
      Seq(
		"import controllers.routes._",
        "import models._",
        "import play.api._"
      ),
      Seq(
      ),
      Some("webTmpl")
    )
  )
}