from agentic.main import main


def test_main_prints_banner(capsys):
    main()
    assert "agentic-python" in capsys.readouterr().out
